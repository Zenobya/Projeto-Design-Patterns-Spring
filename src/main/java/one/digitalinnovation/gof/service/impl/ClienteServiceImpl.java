package one.digitalinnovation.gof.service.impl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;

/**
 * Implementação da Strategy {@link ClienteService}.
 * Singleton gerenciado pelo Spring via @Service.
 * Facade: abstrai integração com banco de dados e API ViaCEP.
 */
@Service
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;

	@Override
	public Iterable<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + id));
	}

	@Override
	public void inserir(Cliente cliente) {
		salvarClienteComCep(cliente);
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		clienteRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + id));
		cliente.setId(id);
		salvarClienteComCep(cliente);
	}

	@Override
	public void deletar(Long id) {
		clienteRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + id));
		clienteRepository.deleteById(id);
	}

	private void salvarClienteComCep(Cliente cliente) {
		// Valida se o CEP foi informado
		if (cliente.getEndereco() == null || cliente.getEndereco().getCep() == null) {
			throw new IllegalArgumentException("CEP não informado.");
		}

		// Valida formato do CEP (somente 8 dígitos numéricos)
		String cep = cliente.getEndereco().getCep().replaceAll("[^0-9]", "");
		if (cep.length() != 8) {
			throw new IllegalArgumentException("CEP inválido: " + cliente.getEndereco().getCep());
		}

		// Busca o endereço no banco ou consulta o ViaCEP
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});

		cliente.setEndereco(endereco);
		clienteRepository.save(cliente);
	}
}