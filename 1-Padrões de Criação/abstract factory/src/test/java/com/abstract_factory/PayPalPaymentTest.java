package com.abstract_factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PayPalPaymentTest {

    private PayPalPayment payPalPayment;

    @BeforeEach
    void setUp() {
        payPalPayment = new PayPalPayment();
    }

    @Nested
    @DisplayName("Processamento de Pagamentos")
    class ProcessamentoTests {

        @Test
        @DisplayName("Deve processar pagamento válido com sucesso alterando status para APPROVED")
        void deveProcessarPagamentoValidoComSucesso() {
            payPalPayment.setEmail("usuario@email.com");
            double valorEsperado = 100.0;

            payPalPayment.processPayment(valorEsperado);

            assertEquals(valorEsperado, payPalPayment.getAmount());
            assertEquals(PaymentStatus.APPROVED, payPalPayment.getStatus());
            assertNotNull(payPalPayment.getTransactionDate());
            assertNotNull(payPalPayment.getTransactionId());
            assertTrue(payPalPayment.getTransactionId().startsWith("PP-"));
        }

        @Test
        @DisplayName("Deve gerar ID de transação único a cada processamento")
        void deveGerarIdTransacaoUnico() {
            payPalPayment.setEmail("usuario@email.com");

            payPalPayment.processPayment(50.0);
            String primeiroId = payPalPayment.getTransactionId();

            PayPalPayment segundoPayment = new PayPalPayment();
            segundoPayment.setEmail("outro@email.com");
            segundoPayment.processPayment(75.0);
            String segundoId = segundoPayment.getTransactionId();

            assertNotEquals(primeiroId, segundoId);
            assertTrue(primeiroId.startsWith("PP-"));
            assertTrue(segundoId.startsWith("PP-"));
        }

        @Test
        @DisplayName("Deve atualizar data de transação durante processamento")
        void deveAtualizarDataTransacao() {
            Date dataAntes = new Date();
            payPalPayment.setEmail("usuario@email.com");

            payPalPayment.processPayment(100.0);

            assertNotNull(payPalPayment.getTransactionDate());
            assertTrue(payPalPayment.getTransactionDate().getTime() >= dataAntes.getTime());
        }
    }

    @Nested
    @DisplayName("Validação de Pagamentos")
    class ValidacaoTests {

        @Test
        @DisplayName("Deve validar pagamento com email válido e valor positivo")
        void deveValidarPagamentoValido() {
            payPalPayment.setEmail("usuario@dominio.com");
            payPalPayment.setAmount(100.0);

            assertTrue(payPalPayment.validatePayment());
            assertTrue(payPalPayment.isPaymentValid());
        }

        @Test
        @DisplayName("Deve rejeitar pagamento com email inválido")
        void deveRejeitarEmailInvalido() {
            payPalPayment.setEmail("emailinvalido");
            payPalPayment.setAmount(100.0);

            assertFalse(payPalPayment.validatePayment());
            assertFalse(payPalPayment.isPaymentValid());
        }

        @Test
        @DisplayName("Deve rejeitar pagamento com email nulo")
        void deveRejeitarEmailNulo() {
            payPalPayment.setEmail(null);
            payPalPayment.setAmount(100.0);

            assertFalse(payPalPayment.validatePayment());
        }

        @Test
        @DisplayName("Deve rejeitar pagamento com valor zero ou negativo")
        void deveRejeitarValorInvalido() {
            payPalPayment.setEmail("usuario@email.com");

            payPalPayment.setAmount(0);
            assertFalse(payPalPayment.validatePayment());

            payPalPayment.setAmount(-50.0);
            assertFalse(payPalPayment.validatePayment());
        }

        @Test
        @DisplayName("Deve retornar lista de erros para dados inválidos")
        void deveRetornarErrosValidacao() {
            payPalPayment.setEmail("emailsemarroba");
            payPalPayment.setAmount(-10.0);

            List<String> erros = payPalPayment.getValidationErrors();

            assertEquals(2, erros.size());
            assertTrue(erros.contains("Email inválido"));
            assertTrue(erros.contains("Valor deve ser maior que zero"));
        }

        @Test
        @DisplayName("Deve retornar lista vazia para dados válidos")
        void deveRetornarListaVaziaParaDadosValidos() {
            payPalPayment.setEmail("usuario@email.com");
            payPalPayment.setAmount(100.0);

            List<String> erros = payPalPayment.getValidationErrors();

            assertTrue(erros.isEmpty());
        }
    }

    @Nested
    @DisplayName("Informações de Pagamento")
    class InformacoesTests {

        @Test
        @DisplayName("Deve retornar informações corretas do pagamento")
        void deveRetornarInformacoesCorretas() {
            assertEquals("PayPal", payPalPayment.getPaymentMethod());
            assertEquals(4, payPalPayment.getMaxInstallments());
        }

        @Test
        @DisplayName("Deve calcular taxa de transação corretamente")
        void deveCalcularTaxaTransacao() {
            payPalPayment.setAmount(100.0);

            double taxa = payPalPayment.getTransactionFee();

            assertEquals(4.0, taxa, 0.01);
        }

        @Test
        @DisplayName("Deve inicializar com status PENDING por padrão")
        void deveInicializarComStatusPending() {
            assertEquals(PaymentStatus.PENDING, payPalPayment.getStatus());
        }
    }

    @Nested
    @DisplayName("Gerenciamento de Status")
    class StatusTests {

        @Test
        @DisplayName("Deve cancelar pagamento alterando status para CANCELLED")
        void deveCancelarPagamento() {
            payPalPayment.setStatus(PaymentStatus.APPROVED);

            payPalPayment.cancelPayment();

            assertEquals(PaymentStatus.CANCELLED, payPalPayment.getStatus());
        }

        @Test
        @DisplayName("Deve processar reembolso alterando status para REFUNDED")
        void deveProcessarReembolso() {
            payPalPayment.setStatus(PaymentStatus.APPROVED);

            payPalPayment.refundPayment();

            assertEquals(PaymentStatus.REFUNDED, payPalPayment.getStatus());
        }
    }

    @Nested
    @DisplayName("Configuração de Detalhes")
    class DetalhesTests {

        @Test
        @DisplayName("Deve definir detalhes personalizados do pagamento")
        void deveDefinirDetalhesPersonalizados() {
            Map<String, Object> detalhes = new HashMap<>();
            detalhes.put("cliente", "João Silva");
            detalhes.put("produto", "Notebook");
            detalhes.put("categoria", "Eletrônicos");

            payPalPayment.setPaymentDetails(detalhes);

            assertEquals(detalhes, payPalPayment.getDetails());
            assertEquals("João Silva", payPalPayment.getDetails().get("cliente"));
        }

        @Test
        @DisplayName("Deve inicializar com mapa de detalhes vazio")
        void deveInicializarComDetalhesVazios() {
            assertNotNull(payPalPayment.getDetails());
            assertTrue(payPalPayment.getDetails().isEmpty());
        }
    }

    @Nested
    @DisplayName("Construtor com Parâmetros")
    class ConstrutorTests {

        @Test
        @DisplayName("Deve criar instância com todos os parâmetros fornecidos")
        void deveCriarInstanciaComParametros() {
            String email = "teste@email.com";
            double valor = 250.0;
            PaymentStatus status = PaymentStatus.APPROVED;
            String transactionId = "PP-12345678";
            Date data = new Date();
            Map<String, Object> detalhes = new HashMap<>();
            detalhes.put("teste", "valor");

            PayPalPayment payment = new PayPalPayment(email, valor, status, transactionId, data, detalhes);

            assertEquals(email, payment.getEmail());
            assertEquals(valor, payment.getAmount());
            assertEquals(status, payment.getStatus());
            assertEquals(transactionId, payment.getTransactionId());
            assertEquals(data, payment.getTransactionDate());
            assertEquals(detalhes, payment.getDetails());
        }
    }

    @Nested
    @DisplayName("Integração de Funcionalidades")
    class IntegracaoTests {

        @Test
        @DisplayName("Deve processar fluxo completo de pagamento válido")
        void deveProcessarFluxoCompletoValido() {
            payPalPayment.setEmail("cliente@empresa.com");
            payPalPayment.setAmount(100.0);

            assertTrue(payPalPayment.validatePayment());
            assertTrue(payPalPayment.getValidationErrors().isEmpty());

            payPalPayment.processPayment(150.0);

            assertEquals(PaymentStatus.APPROVED, payPalPayment.getStatus());
            assertEquals(150.0, payPalPayment.getAmount());
            assertEquals(6.0, payPalPayment.getTransactionFee(), 0.01);
            assertNotNull(payPalPayment.getTransactionId());
            assertTrue(payPalPayment.getTransactionId().startsWith("PP-"));
        }

        @Test
        @DisplayName("Deve bloquear processamento para dados inválidos mas ainda alterar valores internos")
        void deveProcessarMesmoComDadosInvalidos() {
            payPalPayment.setEmail("emailinvalido");

            assertFalse(payPalPayment.validatePayment());
            assertFalse(payPalPayment.getValidationErrors().isEmpty());

            payPalPayment.processPayment(100.0);

            assertEquals(PaymentStatus.APPROVED, payPalPayment.getStatus());
            assertEquals(100.0, payPalPayment.getAmount());
            assertNotNull(payPalPayment.getTransactionId());
        }
    }
}