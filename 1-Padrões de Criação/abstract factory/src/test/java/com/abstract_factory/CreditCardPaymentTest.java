package com.abstract_factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreditCardPaymentTest {

    @Test
    @DisplayName("Deve aprovar pagamento válido com cartão VISA")
    void deveAprovarPagamentoVisa() {
        CreditCardPayment pagamento = new CreditCardPayment("4111111111111111", "João Silva", "12/30", "123", 500.0);
        pagamento.processPayment(500.0);

        assertEquals(PaymentStatus.APPROVED, pagamento.getStatus());
        assertTrue(pagamento.getValidationErrors().isEmpty());
        assertNotNull(pagamento.getTransactionId());
    }

    @Test
    @DisplayName("Deve recusar pagamento com cartão expirado")
    void deveRecusarPagamentoCartaoExpirado() {
        CreditCardPayment pagamento = new CreditCardPayment("4111111111111111", "João Silva", "01/20", "123", 500.0);
        pagamento.processPayment(500.0);

        assertEquals(PaymentStatus.DECLINED, pagamento.getStatus());
        assertTrue(pagamento.getValidationErrors().stream().anyMatch(msg -> msg.contains("Cartão expirado")));
    }

    @Test
    @DisplayName("Deve recusar pagamento com valor abaixo do mínimo")
    void deveRecusarPagamentoValorBaixo() {
        CreditCardPayment pagamento = new CreditCardPayment("4111111111111111", "João Silva", "12/30", "123", 0.5);
        pagamento.processPayment(0.5);

        assertEquals(PaymentStatus.DECLINED, pagamento.getStatus());
        assertTrue(
                pagamento.getValidationErrors().stream().anyMatch(msg -> msg.contains("Valor mínimo para transação")));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar pagamento já aprovado")
    void deveLancarExcecaoCancelarAprovado() {
        CreditCardPayment pagamento = new CreditCardPayment("4111111111111111", "João Silva", "12/30", "123", 500.0);
        pagamento.processPayment(500.0);

        assertThrows(IllegalStateException.class, pagamento::cancelPayment);
    }

    @Test
    @DisplayName("Deve permitir estorno dentro do prazo")
    void devePermitirEstornoDentroPrazo() {
        CreditCardPayment pagamento = new CreditCardPayment("4111111111111111", "João Silva", "12/30", "123", 500.0);
        pagamento.processPayment(500.0);
        pagamento.refundPayment();

        assertEquals(PaymentStatus.REFUNDED, pagamento.getStatus());
        assertNotNull(pagamento.getDetails().get("refund_date"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao estornar pagamento não aprovado")
    void deveLancarExcecaoEstornoNaoAprovado() {
        CreditCardPayment pagamento = new CreditCardPayment("1234123412341234", "João Silva", "12/30", "123", 500.0);
        assertThrows(IllegalStateException.class, pagamento::refundPayment);
    }

    @Test
    @DisplayName("Deve validar corretamente CVV de American Express com 4 dígitos")
    void deveValidarCvvAmexCorreto() {
        CreditCardPayment pagamento = new CreditCardPayment("3411123412341234", "Maria Oliveira", "11/30", "1234",
                500.0);
        assertTrue(pagamento.validatePayment());
    }

    @Test
    @DisplayName("Deve invalidar CVV de American Express com 3 dígitos")
    void deveInvalidarCvvAmexIncorreto() {
        CreditCardPayment pagamento = new CreditCardPayment("3411123412341234", "Maria Oliveira", "11/30", "123", 500.0);
        assertFalse(pagamento.validatePayment());
        assertTrue(pagamento.getValidationErrors().contains("CVV inválido"));
    }

    @Test
    @DisplayName("Deve identificar corretamente o tipo do cartão como Mastercard")
    void deveIdentificarCartaoMastercard() {
        CreditCardPayment pagamento = new CreditCardPayment("5105105105105100", "Lucas Rocha", "10/30", "123", 500.0);
        assertEquals(CardType.MASTERCARD, pagamento.getCardType());
    }

    @Test
    @DisplayName("Deve calcular corretamente valor das parcelas")
    void deveCalcularValorParcelas() {
        CreditCardPayment pagamento = new CreditCardPayment("4111111111111111", "João Silva", "12/30", "123", 1200.0);
        pagamento.setInstallments(3);
        assertEquals(400.0, pagamento.getInstallmentAmount().getAmount().doubleValue());
    }

    @Test
    @DisplayName("Deve lançar exceção ao alterar parcelas após processamento")
    void deveLancarExcecaoAlterarParcelasAposProcessamento() {
        CreditCardPayment pagamento = new CreditCardPayment("4111111111111111", "João Silva", "12/30", "123", 100.0);
        pagamento.processPayment(100.0);
        assertThrows(IllegalStateException.class, () -> pagamento.setInstallments(2));
    }

    @Test
    @DisplayName("Deve recusar transação suspeita por horário e valor alto")
    void deveRecusarTransacaoSuspeitaHorario() {
        CreditCardPayment pagamento = new CreditCardPayment("4111111111111111", "João Silva", "12/30", "123", 5000.0);
        pagamento.processPayment(5000.0);

        if (LocalDateTime.now().getHour() >= 2 && LocalDateTime.now().getHour() <= 5) {
            assertEquals(PaymentStatus.DECLINED, pagamento.getStatus());
            assertTrue(pagamento.getValidationErrors().stream().anyMatch(msg -> msg.contains("Transação suspeita")));
        } else {
            assertNotEquals(PaymentStatus.DECLINED, pagamento.getStatus());
        }
    }

    @Test
    @DisplayName("Deve recusar pagamento com número de parcelas acima do máximo")
    void deveRecusarPagamentoParcelasAcimaLimite() {
        CreditCardPayment pagamento = new CreditCardPayment("4111111111111111", "João Silva", "12/30", "123", 1000.0);
        pagamento.setInstallments(20);
        pagamento.processPayment(1000.0);

        assertEquals(PaymentStatus.DECLINED, pagamento.getStatus());
        assertTrue(
                pagamento.getValidationErrors().stream().anyMatch(msg -> msg.contains("Número de parcelas inválido")));
    }

}