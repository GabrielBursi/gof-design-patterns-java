package com.abstract_factory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreditCardPayment implements
        PaymentProcessor,
        PaymentValidator,
        PaymentInfo,
        BalanceChecker,
        PaymentRefundable,
        PaymentConfigurable {

    private static final double FEE_AMERICAN_EXPRESS = 0.0399;
    private static final double FEE_VISA_MASTERCARD = 0.0329;
    private static final double FEE_ELO = 0.0289;
    private static final double FEE_UNKNOWN = 0.0259;
    private static final double INSTALLMENT_FEE_INCREMENT = 0.005;

    private static final double CREDIT_LIMIT_VISA_MASTERCARD = 15000.0;
    private static final double CREDIT_LIMIT_AMERICAN_EXPRESS = 25000.0;
    private static final double CREDIT_LIMIT_ELO = 10000.0;
    private static final double CREDIT_LIMIT_UNKNOWN = 5000.0;

    private static final double DAILY_LIMIT_AMERICAN_EXPRESS = 10000.0;
    private static final double DAILY_LIMIT_VISA_MASTERCARD = 8000.0;
    private static final double DAILY_LIMIT_OTHERS = 2000.0;

    private static final int MAX_INSTALLMENTS_AMERICAN_EXPRESS = 18;
    private static final int MAX_INSTALLMENTS_VISA_MASTERCARD = 12;
    private static final int MAX_INSTALLMENTS_ELO = 10;
    private static final int MAX_INSTALLMENTS_DEFAULT = 6;

    private static final int MAX_TRANSACTIONS_AMERICAN_EXPRESS = 20;
    private static final int MAX_TRANSACTIONS_VISA_MASTERCARD = 15;
    private static final int MAX_TRANSACTIONS_DEFAULT = 10;

    private static final double MIN_TRANSACTION_AMOUNT = 1.0;
    private static final double MAX_TRANSACTION_AMERICAN_EXPRESS = 20000.0;
    private static final double MAX_TRANSACTION_VISA_MASTERCARD = 15000.0;
    private static final double MAX_TRANSACTION_DEFAULT = 10000.0;

    private static final int REFUND_EXPIRATION_DAYS = 30;
    private static final int SUSPICIOUS_HOUR_START = 2;
    private static final int SUSPICIOUS_HOUR_END = 5;
    private static final double SUSPICIOUS_AMOUNT_THRESHOLD = 1000.0;
    private static final double UNKNOWN_CARD_SUSPICIOUS_AMOUNT = 5000.0;

    private CardNumber cardNumber;
    private String cardHolder;
    private String expirationDate;
    private String cvv;
    private Money amount;
    private CardType cardType;
    private int installments = 1;

    private PaymentStatus status = PaymentStatus.PENDING;
    private String transactionId;
    private LocalDateTime transactionDate;
    private LocalDateTime lastStatusChange;

    private int dailyTransactionCount = 0;
    private Money dailyTransactionAmount = new Money(0.0);

    private Map<String, Object> details = new HashMap<>();
    private List<String> validationErrors = new ArrayList<>();

    public CreditCardPayment(String cardNumber, String cardHolder, String expirationDate, String cvv, double amount) {
        this.cardNumber = new CardNumber(cardNumber);
        this.cardHolder = cardHolder;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.amount = new Money(amount);
        this.cardType = identifyCardType();
        this.transactionDate = LocalDateTime.now();
        this.lastStatusChange = LocalDateTime.now();
    }

    @Override
    public void processPayment(double amount) {
        this.amount = new Money(amount);
        this.transactionDate = LocalDateTime.now();
        this.transactionId = generateTransactionId();

        changeStatus(PaymentStatus.PROCESSING);

        if (!validatePayment()) {
            changeStatus(PaymentStatus.DECLINED);
            return;
        }

        if (isCardExpired()) {
            addValidationError("Cartão expirado");
            changeStatus(PaymentStatus.DECLINED);
            return;
        }

        if (isSuspiciousTransaction()) {
            addValidationError("Transação suspeita - bloqueada por segurança");
            changeStatus(PaymentStatus.DECLINED);
            return;
        }

        if (!hasEnoughBalance()) {
            addValidationError("Limite de crédito insuficiente");
            changeStatus(PaymentStatus.DECLINED);
            return;
        }

        if (exceedsDailyLimit()) {
            addValidationError("Limite diário de transações excedido");
            changeStatus(PaymentStatus.DECLINED);
            return;
        }

        changeStatus(PaymentStatus.APPROVED);
        updateDailyLimits();

        System.out.println("Pagamento aprovado no cartão: " + maskCardNumber() + " - Valor: " + this.amount);
    }

    @Override
    public void cancelPayment() {
        if (status == PaymentStatus.APPROVED || status == PaymentStatus.REFUNDED) {
            throw new IllegalStateException("Não é possível cancelar pagamento " + status.name().toLowerCase());
        }

        changeStatus(PaymentStatus.CANCELLED);
        addValidationError("Pagamento cancelado pelo usuário");
    }

    @Override
    public void refundPayment() {
        if (status != PaymentStatus.APPROVED) {
            throw new IllegalStateException("Só é possível estornar pagamentos aprovados");
        }

        if (transactionDate.isBefore(LocalDateTime.now().minusDays(REFUND_EXPIRATION_DAYS))) {
            throw new IllegalStateException("Prazo para estorno expirado (30 dias)");
        }

        changeStatus(PaymentStatus.REFUNDED);
        details.put("refund_date", LocalDateTime.now().toString());
    }

    @Override
    public boolean validatePayment() {
        validationErrors.clear();
        boolean isValid = true;

        if (cardNumber == null) {
            addValidationError("Número do cartão é obrigatório");
            isValid = false;
        }

        if (cardHolder == null || cardHolder.trim().length() < 2) {
            addValidationError("Nome do portador deve ter pelo menos 2 caracteres");
            isValid = false;
        }

        if (!isValidExpirationDate()) {
            addValidationError("Data de expiração inválida (formato MM/YY)");
            isValid = false;
        }

        if (!isValidCvv()) {
            addValidationError("CVV inválido");
            isValid = false;
        }

        if (amount == null || amount.getAmount().doubleValue() <= 0) {
            addValidationError("Valor deve ser maior que zero");
            isValid = false;
        }

        if (amount != null) {
            if (amount.getAmount().doubleValue() < getMinTransactionAmount()) {
                addValidationError("Valor mínimo para transação: R$ " + getMinTransactionAmount());
                isValid = false;
            }

            if (amount.getAmount().doubleValue() > getMaxTransactionAmount()) {
                addValidationError("Valor máximo para transação: R$ " + getMaxTransactionAmount());
                isValid = false;
            }
        }

        if (installments < 1 || installments > getMaxInstallments()) {
            addValidationError("Número de parcelas inválido (1 a " + getMaxInstallments() + ")");
            isValid = false;
        }

        return isValid;
    }

    @Override
    public boolean hasEnoughBalance() {
        double creditLimit = getCreditLimit();
        double totalAmount = amount.getAmount().doubleValue() + getTransactionFee();
        return totalAmount <= creditLimit;
    }

    @Override
    public double getTransactionFee() {
        double baseFee = switch (cardType) {
            case AMERICAN_EXPRESS -> FEE_AMERICAN_EXPRESS;
            case VISA, MASTERCARD -> FEE_VISA_MASTERCARD;
            case ELO -> FEE_ELO;
            default -> FEE_UNKNOWN;
        };

        if (installments > 1) {
            baseFee += INSTALLMENT_FEE_INCREMENT * (installments - 1);
        }

        return amount.getAmount().doubleValue() * baseFee;
    }

    @Override
    public int getMaxInstallments() {
        return switch (cardType) {
            case AMERICAN_EXPRESS -> MAX_INSTALLMENTS_AMERICAN_EXPRESS;
            case VISA, MASTERCARD -> MAX_INSTALLMENTS_VISA_MASTERCARD;
            case ELO -> MAX_INSTALLMENTS_ELO;
            default -> MAX_INSTALLMENTS_DEFAULT;
        };
    }

    @Override
    public PaymentStatus getStatus() {
        return status;
    }

    @Override
    public double getAmount() {
        return amount != null ? amount.getAmount().doubleValue() : 0.0;
    }

    @Override
    public String getPaymentMethod() {
        return "Credit Card - " + (cardType != null ? cardType.name() : "UNKNOWN");
    }

    @Override
    public Date getTransactionDate() {
        return transactionDate != null
                ? Date.from(transactionDate.atZone(java.time.ZoneId.systemDefault()).toInstant())
                : null;
    }

    @Override
    public String generateTransactionId() {
        String prefix = switch (cardType) {
            case VISA -> "VI";
            case MASTERCARD -> "MC";
            case AMERICAN_EXPRESS -> "AX";
            case ELO -> "EL";
            default -> "CC";
        };
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public boolean isPaymentValid() {
        return validatePayment();
    }

    @Override
    public List<String> getValidationErrors() {
        return new ArrayList<>(validationErrors);
    }

    @Override
    public void setPaymentDetails(Map<String, Object> details) {
        this.details.putAll(details);
    }

    public void setInstallments(int installments) {
        if (status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Não é possível alterar parcelas após processamento");
        }
        this.installments = installments;
    }

    public Money getInstallmentAmount() {
        if (amount == null || installments <= 0)
            return new Money(0.0);
        return new Money(amount.getAmount().doubleValue() / installments);
    }

    public boolean isExpired() {
        return isCardExpired();
    }

    private void changeStatus(PaymentStatus newStatus) {
        this.status = newStatus;
        this.lastStatusChange = LocalDateTime.now();
        details.put("status_history", getStatusHistory());
    }

    private void updateDailyLimits() {
        this.dailyTransactionCount++;
        this.dailyTransactionAmount = new Money(
                dailyTransactionAmount.getAmount().doubleValue() + amount.getAmount().doubleValue());
    }

    private boolean isCardExpired() {
        if (expirationDate == null || !expirationDate.matches("\\d{2}/\\d{2}"))
            return true;

        try {
            String[] parts = expirationDate.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = 2000 + Integer.parseInt(parts[1]);
            LocalDate expDate = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);
            return LocalDate.now().isAfter(expDate);
        } catch (Exception e) {
            return true;
        }
    }

    private boolean isSuspiciousTransaction() {
        int hour = LocalDateTime.now().getHour();
        if (hour >= SUSPICIOUS_HOUR_START && hour <= SUSPICIOUS_HOUR_END &&
                amount.getAmount().doubleValue() > SUSPICIOUS_AMOUNT_THRESHOLD)
            return true;

        if (cardType == CardType.UNKNOWN && amount.getAmount().doubleValue() > UNKNOWN_CARD_SUSPICIOUS_AMOUNT)
            return true;

        return dailyTransactionCount > MAX_TRANSACTIONS_DEFAULT;
    }

    private boolean exceedsDailyLimit() {
        double dailyLimit = getDailyLimit();
        double newDailyTotal = dailyTransactionAmount.getAmount().doubleValue() + amount.getAmount().doubleValue();
        return newDailyTotal > dailyLimit || dailyTransactionCount >= getMaxDailyTransactions();
    }

    private boolean isValidExpirationDate() {
        return expirationDate != null && expirationDate.matches("^(0[1-9]|1[0-2])/\\d{2}$");
    }

    private boolean isValidCvv() {
        if (cvv == null)
            return false;
        if (cardType == CardType.AMERICAN_EXPRESS)
            return cvv.matches("\\d{4}");
        return cvv.matches("\\d{3}");
    }

    private CardType identifyCardType() {
        if (cardNumber == null)
            return CardType.UNKNOWN;
        String number = cardNumber.getValue();

        if (number.startsWith("4"))
            return CardType.VISA;
        if (number.startsWith("5") || number.matches("^2[2-7].*"))
            return CardType.MASTERCARD;
        if (number.startsWith("34") || number.startsWith("37"))
            return CardType.AMERICAN_EXPRESS;
        if (number.startsWith("50") || number.startsWith("636368"))
            return CardType.ELO;

        return CardType.UNKNOWN;
    }

    private double getCreditLimit() {
        return switch (cardType) {
            case VISA, MASTERCARD -> CREDIT_LIMIT_VISA_MASTERCARD;
            case AMERICAN_EXPRESS -> CREDIT_LIMIT_AMERICAN_EXPRESS;
            case ELO -> CREDIT_LIMIT_ELO;
            default -> CREDIT_LIMIT_UNKNOWN;
        };
    }

    private double getDailyLimit() {
        return switch (cardType) {
            case AMERICAN_EXPRESS -> DAILY_LIMIT_AMERICAN_EXPRESS;
            case VISA, MASTERCARD -> DAILY_LIMIT_VISA_MASTERCARD;
            default -> DAILY_LIMIT_OTHERS;
        };
    }

    private int getMaxDailyTransactions() {
        return switch (cardType) {
            case AMERICAN_EXPRESS -> MAX_TRANSACTIONS_AMERICAN_EXPRESS;
            case VISA, MASTERCARD -> MAX_TRANSACTIONS_VISA_MASTERCARD;
            default -> MAX_TRANSACTIONS_DEFAULT;
        };
    }

    private double getMinTransactionAmount() {
        return MIN_TRANSACTION_AMOUNT;
    }

    private double getMaxTransactionAmount() {
        return switch (cardType) {
            case AMERICAN_EXPRESS -> MAX_TRANSACTION_AMERICAN_EXPRESS;
            case VISA, MASTERCARD -> MAX_TRANSACTION_VISA_MASTERCARD;
            default -> MAX_TRANSACTION_DEFAULT;
        };
    }

    private void addValidationError(String error) {
        validationErrors.add(error);
        details.put("last_error", error);
        details.put("error_timestamp", LocalDateTime.now().toString());
    }

    private String getStatusHistory() {
        return details.getOrDefault("status_history", "") +
                status.name() + " em " +
                lastStatusChange.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "; ";
    }

    private String maskCardNumber() {
        return cardNumber != null ? cardNumber.getMasked() : "****";
    }
}
