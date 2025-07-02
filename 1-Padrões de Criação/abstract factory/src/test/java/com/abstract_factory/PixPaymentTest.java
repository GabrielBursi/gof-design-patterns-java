package com.abstract_factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PixPaymentTest {

    private PixPayment pixPayment;

    @BeforeEach
    void setUp() {
        pixPayment = new PixPayment();
    }

    @Nested
    @DisplayName("Processamento de Pagamentos")
    class ProcessamentoTests {

        @Test
        @DisplayName("Deve processar pagamento PIX com sucesso alterando status para APPROVED")
        void deveProcessarPixComSucesso() {
            pixPayment.setPixKey("usuario@email.com");
            double valorEsperado = 100.0;

            pixPayment.processPayment(valorEsperado);

            assertEquals(valorEsperado, pixPayment.getAmount());
            assertEquals(PaymentStatus.APPROVED, pixPayment.getStatus());
            assertNotNull(pixPayment.getTransactionDate());
            assertNotNull(pixPayment.getTransactionId());
            assertTrue(pixPayment.getTransactionId().startsWith("PIX-"));
        }

        @Test
        @DisplayName("Deve gerar ID de transação único a cada processamento PIX")
        void deveGerarIdTransacaoUnicoPix() {
            pixPayment.setPixKey("11999999999");

            pixPayment.processPayment(50.0);
            String primeiroId = pixPayment.getTransactionId();

            PixPayment segundoPayment = new PixPayment();
            segundoPayment.setPixKey("outro@email.com");
            segundoPayment.processPayment(75.0);
            String segundoId = segundoPayment.getTransactionId();

            assertNotEquals(primeiroId, segundoId);
            assertTrue(primeiroId.startsWith("PIX-"));
            assertTrue(segundoId.startsWith("PIX-"));
        }

        @Test
        @DisplayName("Deve registrar data de transação durante processamento PIX")
        void deveRegistrarDataTransacaoPix() {
            Date dataAntes = new Date();
            pixPayment.setPixKey("123.456.789-00");

            pixPayment.processPayment(200.0);

            assertNotNull(pixPayment.getTransactionDate());
            assertTrue(pixPayment.getTransactionDate().getTime() >= dataAntes.getTime());
        }

        @Test
        @DisplayName("Deve processar PIX mesmo com valor decimal")
        void deveProcessarPixComValorDecimal() {
            pixPayment.setPixKey("usuario@email.com");
            double valorDecimal = 99.99;

            pixPayment.processPayment(valorDecimal);

            assertEquals(valorDecimal, pixPayment.getAmount());
            assertEquals(PaymentStatus.APPROVED, pixPayment.getStatus());
        }
    }

    @Nested
    @DisplayName("Validação de Chave PIX")
    class ValidacaoChaveTests {

        @Test
        @DisplayName("Deve validar PIX com chave email válida")
        void deveValidarChaveEmail() {
            pixPayment.setPixKey("usuario@dominio.com");
            pixPayment.setAmount(100.0);

            assertTrue(pixPayment.validatePayment());
            assertTrue(pixPayment.isPaymentValid());
        }

        @Test
        @DisplayName("Deve validar PIX com chave telefone válida")
        void deveValidarChaveTelefone() {
            pixPayment.setPixKey("11999999999");
            pixPayment.setAmount(50.0);

            assertTrue(pixPayment.validatePayment());
            assertTrue(pixPayment.isPaymentValid());
        }

        @Test
        @DisplayName("Deve validar PIX com chave CPF válida")
        void deveValidarChaveCpf() {
            pixPayment.setPixKey("12345678901");
            pixPayment.setAmount(150.0);

            assertTrue(pixPayment.validatePayment());
            assertTrue(pixPayment.isPaymentValid());
        }

        @Test
        @DisplayName("Deve validar PIX com chave aleatória válida")
        void deveValidarChaveAleatoria() {
            pixPayment.setPixKey("a1b2c3d4-e5f6-7g8h-9i0j-k1l2m3n4o5p6");
            pixPayment.setAmount(75.0);

            assertTrue(pixPayment.validatePayment());
            assertTrue(pixPayment.isPaymentValid());
        }

        @Test
        @DisplayName("Deve rejeitar PIX com chave nula")
        void deveRejeitarChaveNula() {
            pixPayment.setPixKey(null);
            pixPayment.setAmount(100.0);

            assertFalse(pixPayment.validatePayment());
            assertFalse(pixPayment.isPaymentValid());
        }

        @Test
        @DisplayName("Deve rejeitar PIX com chave vazia")
        void deveRejeitarChaveVazia() {
            pixPayment.setPixKey("");
            pixPayment.setAmount(100.0);

            assertFalse(pixPayment.validatePayment());
            assertFalse(pixPayment.isPaymentValid());
        }

        @Test
        @DisplayName("Deve rejeitar PIX com chave contendo apenas espaços")
        void deveRejeitarChaveComEspacos() {
            pixPayment.setPixKey("   ");
            pixPayment.setAmount(100.0);

            assertFalse(pixPayment.validatePayment());
        }
    }

    @Nested
    @DisplayName("Validação de Valor")
    class ValidacaoValorTests {

        @Test
        @DisplayName("Deve rejeitar PIX com valor zero")
        void deveRejeitarValorZero() {
            pixPayment.setPixKey("usuario@email.com");
            pixPayment.setAmount(0);

            assertFalse(pixPayment.validatePayment());
        }

        @Test
        @DisplayName("Deve rejeitar PIX com valor negativo")
        void deveRejeitarValorNegativo() {
            pixPayment.setPixKey("11999999999");
            pixPayment.setAmount(-50.0);

            assertFalse(pixPayment.validatePayment());
        }

        @Test
        @DisplayName("Deve aceitar PIX com valor mínimo positivo")
        void deveAceitarValorMinimoPositivo() {
            pixPayment.setPixKey("123.456.789-00");
            pixPayment.setAmount(0.01);

            assertTrue(pixPayment.validatePayment());
        }
    }

    @Nested
    @DisplayName("Erros de Validação")
    class ErrosValidacaoTests {

        @Test
        @DisplayName("Deve retornar erro para chave PIX obrigatória quando nula")
        void deveRetornarErroChaveObrigatoriaNula() {
            pixPayment.setPixKey(null);
            pixPayment.setAmount(100.0);

            List<String> erros = pixPayment.getValidationErrors();

            assertEquals(1, erros.size());
            assertTrue(erros.contains("Chave PIX é obrigatória"));
        }

        @Test
        @DisplayName("Deve retornar erro para chave PIX obrigatória quando vazia")
        void deveRetornarErroChaveObrigatoriaVazia() {
            pixPayment.setPixKey("");
            pixPayment.setAmount(100.0);

            List<String> erros = pixPayment.getValidationErrors();

            assertEquals(1, erros.size());
            assertTrue(erros.contains("Chave PIX é obrigatória"));
        }

        @Test
        @DisplayName("Deve retornar erro para valor inválido")
        void deveRetornarErroValorInvalido() {
            pixPayment.setPixKey("usuario@email.com");
            pixPayment.setAmount(-10.0);

            List<String> erros = pixPayment.getValidationErrors();

            assertEquals(1, erros.size());
            assertTrue(erros.contains("Valor deve ser maior que zero"));
        }

        @Test
        @DisplayName("Deve retornar múltiplos erros para dados completamente inválidos")
        void deveRetornarMultiplosErros() {
            pixPayment.setPixKey("");
            pixPayment.setAmount(0);

            List<String> erros = pixPayment.getValidationErrors();

            assertEquals(2, erros.size());
            assertTrue(erros.contains("Chave PIX é obrigatória"));
            assertTrue(erros.contains("Valor deve ser maior que zero"));
        }

        @Test
        @DisplayName("Deve retornar lista vazia para PIX válido")
        void deveRetornarListaVaziaParaPixValido() {
            pixPayment.setPixKey("usuario@email.com");
            pixPayment.setAmount(100.0);

            List<String> erros = pixPayment.getValidationErrors();

            assertTrue(erros.isEmpty());
        }
    }

    @Nested
    @DisplayName("Informações do Pagamento")
    class InformacoesTests {

        @Test
        @DisplayName("Deve retornar método de pagamento como PIX")
        void deveRetornarMetodoPagamentoPix() {
            assertEquals("PIX", pixPayment.getPaymentMethod());
        }

        @Test
        @DisplayName("Deve inicializar com status PENDING por padrão")
        void deveInicializarComStatusPending() {
            assertEquals(PaymentStatus.PENDING, pixPayment.getStatus());
        }

        @Test
        @DisplayName("Deve retornar valores corretos após inicialização")
        void deveRetornarValoresCorretos() {
            assertEquals(0.0, pixPayment.getAmount());
            assertNull(pixPayment.getTransactionDate());
            assertNull(pixPayment.getTransactionId());
        }
    }

    @Nested
    @DisplayName("Construtor com Parâmetros")
    class ConstrutorTests {

        @Test
        @DisplayName("Deve criar instância PIX com todos os parâmetros fornecidos")
        void deveCriarInstanciaPixComParametros() {
            String chave = "usuario@email.com";
            double valor = 250.0;
            PaymentStatus status = PaymentStatus.APPROVED;
            String transactionId = "PIX-12345678";
            Date data = new Date();

            PixPayment payment = new PixPayment(chave, valor, status, transactionId, data);

            assertEquals(chave, payment.getPixKey());
            assertEquals(valor, payment.getAmount());
            assertEquals(status, payment.getStatus());
            assertEquals(transactionId, payment.getTransactionId());
            assertEquals(data, payment.getTransactionDate());
        }

        @Test
        @DisplayName("Deve criar instância PIX vazia com construtor sem parâmetros")
        void deveCriarInstanciaPixVazia() {
            PixPayment payment = new PixPayment();

            assertNull(payment.getPixKey());
            assertEquals(0.0, payment.getAmount());
            assertEquals(PaymentStatus.PENDING, payment.getStatus());
            assertNull(payment.getTransactionId());
            assertNull(payment.getTransactionDate());
        }
    }

    @Nested
    @DisplayName("Fluxos Integrados")
    class FluxosIntegradosTests {

        @Test
        @DisplayName("Deve processar fluxo completo de PIX válido")
        void deveProcessarFluxoCompletoPixValido() {
            pixPayment.setPixKey("11999999999");
            pixPayment.setAmount(100);

            assertTrue(pixPayment.validatePayment());
            assertTrue(pixPayment.getValidationErrors().isEmpty());

            pixPayment.processPayment(150.0);

            assertEquals(PaymentStatus.APPROVED, pixPayment.getStatus());
            assertEquals(150.0, pixPayment.getAmount());
            assertNotNull(pixPayment.getTransactionId());
            assertTrue(pixPayment.getTransactionId().startsWith("PIX-"));
            assertNotNull(pixPayment.getTransactionDate());
        }

        @Test
        @DisplayName("Deve processar PIX mesmo com dados inválidos alterando valores internos")
        void deveProcessarPixMesmoComDadosInvalidos() {
            pixPayment.setPixKey("");

            assertFalse(pixPayment.validatePayment());
            assertFalse(pixPayment.getValidationErrors().isEmpty());

            pixPayment.processPayment(100.0);

            assertEquals(PaymentStatus.APPROVED, pixPayment.getStatus());
            assertEquals(100.0, pixPayment.getAmount());
            assertNotNull(pixPayment.getTransactionId());
            assertTrue(pixPayment.getTransactionId().startsWith("PIX-"));
        }

        @Test
        @DisplayName("Deve manter consistência entre generateTransactionId e getTransactionId")
        void deveManterConsistenciaTransactionId() {
            pixPayment.setPixKey("usuario@email.com");
            pixPayment.processPayment(100.0);

            String idGerado = pixPayment.generateTransactionId();
            String idObtido = pixPayment.getTransactionId();

            assertEquals(idGerado, idObtido);
            assertNotNull(idGerado);
            assertTrue(idGerado.startsWith("PIX-"));
        }
    }
}
