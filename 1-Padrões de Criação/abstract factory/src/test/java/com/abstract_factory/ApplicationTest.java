package com.abstract_factory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ApplicationTest {

    @Mock
    private PaymentFactory mockFactory;

    @Mock
    private PaymentProcessor mockProcessor;

    @Mock
    private PaymentInfo mockInfo;

    @Mock
    private PaymentValidator mockValidator;

    @Mock
    private CreditCardPayment mockCreditCard;

    private Application application;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockFactory.createPaymentProcessor()).thenReturn(mockProcessor);
        when(mockFactory.createPaymentInfo()).thenReturn(mockInfo);
        when(mockFactory.createPaymentValidator()).thenReturn(mockValidator);

        application = new Application(mockFactory);

        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Nested
    @DisplayName("Inicialização da Aplicação")
    class InicializacaoTests {

        @Test
        @DisplayName("Deve criar aplicação inicializando componentes através da factory")
        void deveCriarAplicacaoComFactory() {
            verify(mockFactory).createPaymentProcessor();
            verify(mockFactory).createPaymentInfo();
            verify(mockFactory).createPaymentValidator();

            assertNotNull(application);
        }

        @Test
        @DisplayName("Deve criar nova aplicação para cada factory diferente")
        void deveCriarNovaAplicacaoParaCadaFactory() {
            PaymentFactory outraFactory = mock(PaymentFactory.class);
            PaymentProcessor outroProcessor = mock(PaymentProcessor.class);
            PaymentInfo outraInfo = mock(PaymentInfo.class);
            PaymentValidator outroValidator = mock(PaymentValidator.class);

            when(outraFactory.createPaymentProcessor()).thenReturn(outroProcessor);
            when(outraFactory.createPaymentInfo()).thenReturn(outraInfo);
            when(outraFactory.createPaymentValidator()).thenReturn(outroValidator);

            Application outraApplication = new Application(outraFactory);

            verify(outraFactory).createPaymentProcessor();
            verify(outraFactory).createPaymentInfo();
            verify(outraFactory).createPaymentValidator();

            assertNotEquals(application, outraApplication);
        }
    }

    @Nested
    @DisplayName("Processamento de Pagamentos")
    class ProcessamentoTests {

        @Test
        @DisplayName("Deve processar pagamento exibindo informações básicas sem erros")
        void deveProcessarPagamentoComInformacoesBasicas() {
            when(mockInfo.getPaymentMethod()).thenReturn("PIX");
            when(mockProcessor.getStatus()).thenReturn(PaymentStatus.APPROVED);
            when(mockInfo.generateTransactionId()).thenReturn("PIX-12345678");
            when(mockValidator.getValidationErrors()).thenReturn(Collections.emptyList());

            application.processPayment(100.0);

            verify(mockProcessor).processPayment(100.0);
            verify(mockInfo).getPaymentMethod();
            verify(mockProcessor).getStatus();
            verify(mockInfo).generateTransactionId();
            verify(mockValidator).getValidationErrors();

            String output = outputStream.toString();
            assertTrue(output.contains("=== Processando Pagamento ==="));
            assertTrue(output.contains("Método: PIX"));
            assertTrue(output.contains("Status: APPROVED"));
            assertTrue(output.contains("ID: PIX-12345678"));
        }

        @Test
        @DisplayName("Deve processar pagamento e exibir erros de validação quando existirem")
        void deveProcessarPagamentoComErrosValidacao() {
            when(mockInfo.getPaymentMethod()).thenReturn("PayPal");
            when(mockProcessor.getStatus()).thenReturn(PaymentStatus.PENDING);
            when(mockInfo.generateTransactionId()).thenReturn("PP-87654321");
            when(mockValidator.getValidationErrors()).thenReturn(Arrays.asList("Email inválido", "Valor muito baixo"));

            application.processPayment(50.0);

            verify(mockProcessor).processPayment(50.0);

            String output = outputStream.toString();
            assertTrue(output.contains("Método: PayPal"));
            assertTrue(output.contains("Status: PENDING"));
            assertTrue(output.contains("Erros: [Email inválido, Valor muito baixo]"));
        }

        @Test
        @DisplayName("Deve processar diferentes valores de pagamento")
        void deveProcessarDiferentesValores() {
            when(mockInfo.getPaymentMethod()).thenReturn("Cartão");
            when(mockProcessor.getStatus()).thenReturn(PaymentStatus.APPROVED);
            when(mockInfo.generateTransactionId()).thenReturn("CC-11111111");
            when(mockValidator.getValidationErrors()).thenReturn(Collections.emptyList());

            application.processPayment(999.99);

            verify(mockProcessor).processPayment(999.99);

            String output = outputStream.toString();
            assertTrue(output.contains("=== Processando Pagamento ==="));
        }
    }

    @Nested
    @DisplayName("Exibição de Informações")
    class ExibicaoInformacoesTests {

        @Test
        @DisplayName("Deve exibir informações básicas para pagamento não cartão de crédito")
        void deveExibirInformacoesBasicasNaoCartao() {
            when(mockInfo.getPaymentMethod()).thenReturn("PIX");
            when(mockInfo.getAmount()).thenReturn(150.0);
            when(mockProcessor.getStatus()).thenReturn(PaymentStatus.APPROVED);
            when(mockInfo.getTransactionDate()).thenReturn(new Date(1640995200000L));

            application.showPaymentInfo();

            verify(mockInfo).getPaymentMethod();
            verify(mockInfo).getAmount();
            verify(mockProcessor).getStatus();
            verify(mockInfo).getTransactionDate();

            String output = outputStream.toString();
            assertTrue(output.contains("=== Informações do Pagamento ==="));
            assertTrue(output.contains("Método: PIX"));
            assertTrue(output.contains("Valor: R$ 150.0"));
            assertTrue(output.contains("Status: APPROVED"));
        }

        @Test
        @DisplayName("Deve exibir informações completas para cartão de crédito")
        void deveExibirInformacoesCompletasCartaoCredito() {
            when(mockFactory.createPaymentProcessor()).thenReturn(mockCreditCard);
            when(mockInfo.getPaymentMethod()).thenReturn("Cartão de Crédito");
            when(mockInfo.getAmount()).thenReturn(500.0);
            when(mockCreditCard.getStatus()).thenReturn(PaymentStatus.APPROVED);
            when(mockInfo.getTransactionDate()).thenReturn(new Date(1640995200000L));
            when(mockCreditCard.getMaxInstallments()).thenReturn(12);
            when(mockCreditCard.getTransactionFee()).thenReturn(15.0);

            Application appComCartao = new Application(mockFactory);
            appComCartao.showPaymentInfo();

            verify(mockInfo).getPaymentMethod();
            verify(mockInfo).getAmount();
            verify(mockCreditCard).getStatus();
            verify(mockInfo).getTransactionDate();
            verify(mockCreditCard).getMaxInstallments();
            verify(mockCreditCard).getTransactionFee();

            String output = outputStream.toString();
            assertTrue(output.contains("Método: Cartão de Crédito"));
            assertTrue(output.contains("Valor: R$ 500.0"));
            assertTrue(output.contains("Parcelas máximas: 12"));
            assertTrue(output.contains("Taxa: R$ 15,00"));
        }

        @Test
        @DisplayName("Deve exibir informações mesmo com valores nulos")
        void deveExibirInformacoesComValoresNulos() {
            when(mockInfo.getPaymentMethod()).thenReturn("PIX");
            when(mockInfo.getAmount()).thenReturn(0.0);
            when(mockProcessor.getStatus()).thenReturn(PaymentStatus.PENDING);
            when(mockInfo.getTransactionDate()).thenReturn(null);

            application.showPaymentInfo();

            String output = outputStream.toString();
            assertTrue(output.contains("Método: PIX"));
            assertTrue(output.contains("Valor: R$ 0.0"));
            assertTrue(output.contains("Status: PENDING"));
            assertTrue(output.contains("Data: null"));
        }
    }

    @Nested
    @DisplayName("Integração com Diferentes Tipos de Pagamento")
    class IntegracaoTiposTests {

        @Test
        @DisplayName("Deve processar fluxo completo PIX sem erros")
        void deveProcessarFluxoCompletoPix() {
            when(mockInfo.getPaymentMethod()).thenReturn("PIX");
            when(mockInfo.getAmount()).thenReturn(200.0);
            when(mockProcessor.getStatus()).thenReturn(PaymentStatus.APPROVED);
            when(mockInfo.generateTransactionId()).thenReturn("PIX-ABCD1234");
            when(mockInfo.getTransactionDate()).thenReturn(new Date());
            when(mockValidator.getValidationErrors()).thenReturn(Collections.emptyList());

            application.processPayment(200.0);
            application.showPaymentInfo();

            String output = outputStream.toString();
            assertTrue(output.contains("=== Processando Pagamento ==="));
            assertTrue(output.contains("=== Informações do Pagamento ==="));
            assertTrue(output.contains("PIX"));
            assertFalse(output.contains("Parcelas máximas"));
        }

        @Test
        @DisplayName("Deve processar fluxo completo cartão de crédito com informações extras")
        void deveProcessarFluxoCompletoCartaoCredito() {
            when(mockFactory.createPaymentProcessor()).thenReturn(mockCreditCard);
            when(mockInfo.getPaymentMethod()).thenReturn("Cartão de Crédito");
            when(mockInfo.getAmount()).thenReturn(300.0);
            when(mockCreditCard.getStatus()).thenReturn(PaymentStatus.APPROVED);
            when(mockInfo.generateTransactionId()).thenReturn("CC-EFGH5678");
            when(mockInfo.getTransactionDate()).thenReturn(new Date());
            when(mockValidator.getValidationErrors()).thenReturn(Collections.emptyList());
            when(mockCreditCard.getMaxInstallments()).thenReturn(6);
            when(mockCreditCard.getTransactionFee()).thenReturn(9.0);

            Application appCartao = new Application(mockFactory);
            appCartao.processPayment(300.0);
            appCartao.showPaymentInfo();

            String output = outputStream.toString();
            assertTrue(output.contains("Cartão de Crédito"));
            assertTrue(output.contains("Parcelas máximas: 6"));
            assertTrue(output.contains("Taxa: R$ 9,00"));
        }
    }

    @Nested
    @DisplayName("Tratamento de Estados de Pagamento")
    class EstadosPagamentoTests {

        @Test
        @DisplayName("Deve lidar com pagamento pendente e erros de validação")
        void deveLidarComPagamentoPendente() {
            when(mockInfo.getPaymentMethod()).thenReturn("PayPal");
            when(mockProcessor.getStatus()).thenReturn(PaymentStatus.PENDING);
            when(mockInfo.generateTransactionId()).thenReturn("PP-WAIT1234");
            when(mockValidator.getValidationErrors()).thenReturn(Arrays.asList("Aguardando confirmação"));

            application.processPayment(75.0);

            String output = outputStream.toString();
            assertTrue(output.contains("Status: PENDING"));
            assertTrue(output.contains("Erros: [Aguardando confirmação]"));
        }

        @Test
        @DisplayName("Deve lidar com pagamento cancelado")
        void deveLidarComPagamentoCancelado() {
            when(mockInfo.getPaymentMethod()).thenReturn("Cartão");
            when(mockInfo.getAmount()).thenReturn(400.0);
            when(mockProcessor.getStatus()).thenReturn(PaymentStatus.CANCELLED);
            when(mockInfo.getTransactionDate()).thenReturn(new Date());

            application.showPaymentInfo();

            String output = outputStream.toString();
            assertTrue(output.contains("Status: CANCELLED"));
            assertTrue(output.contains("Valor: R$ 400.0"));
        }

        @Test
        @DisplayName("Deve lidar com pagamento reembolsado")
        void deveLidarComPagamentoReembolsado() {
            when(mockInfo.getPaymentMethod()).thenReturn("PayPal");
            when(mockInfo.getAmount()).thenReturn(250.0);
            when(mockProcessor.getStatus()).thenReturn(PaymentStatus.REFUNDED);
            when(mockInfo.getTransactionDate()).thenReturn(new Date());

            application.showPaymentInfo();

            String output = outputStream.toString();
            assertTrue(output.contains("Status: REFUNDED"));
        }
    }

    void tearDown() {
        System.setOut(originalOut);
    }
}
