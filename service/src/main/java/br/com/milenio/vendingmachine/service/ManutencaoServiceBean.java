package br.com.milenio.vendingmachine.service;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.milenio.vendingmachine.domain.model.Manutencao;
import br.com.milenio.vendingmachine.repository.ManutencaoRepository;

@Stateless
public class ManutencaoServiceBean implements ManutencaoService {
	
	@EJB
	ManutencaoRepository manutencaoRepository;
	
	private static final Logger LOGGER = LogManager.getLogger(UsuarioServiceBean.class);
	
	@Override
	public void cadastrar(Manutencao manutencao) {
		manutencao.setDataCadastro(new Date());
		manutencaoRepository.persist(manutencao);
	}
}
