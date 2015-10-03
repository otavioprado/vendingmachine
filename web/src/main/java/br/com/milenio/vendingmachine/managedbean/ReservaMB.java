package br.com.milenio.vendingmachine.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;

import br.com.milenio.vendingmachine.domain.model.Auditoria;
import br.com.milenio.vendingmachine.domain.model.Cliente;
import br.com.milenio.vendingmachine.domain.model.Maquina;
import br.com.milenio.vendingmachine.domain.model.Reserva;
import br.com.milenio.vendingmachine.exceptions.CadastroInexistenteException;
import br.com.milenio.vendingmachine.exceptions.InconsistenciaException;
import br.com.milenio.vendingmachine.security.Seguranca;
import br.com.milenio.vendingmachine.service.AuditoriaService;
import br.com.milenio.vendingmachine.service.ClienteService;
import br.com.milenio.vendingmachine.service.MaquinaService;
import br.com.milenio.vendingmachine.service.ReservaService;

@Named
@ViewScoped
public class ReservaMB implements Serializable {
	private static final long serialVersionUID = 2825836818754743227L;
	
	@Inject
	private Logger logger;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private FacesContext ctx;
	
	@Inject
	private AuditoriaService auditoriaService;
	
	@Inject
	private ReservaService reservaService;
	
	@Inject
	private MaquinaService maquinaService;
	
	@Inject
	private ClienteService clienteService;
	
	private Cliente cliente = new Cliente();
	private Maquina maquina = new Maquina();
	private Reserva reserva = new Reserva();
	private List<Maquina> listMaquinas = new ArrayList<Maquina>();
	private List<Cliente> listClientes = new ArrayList<Cliente>();
	private Reserva reservaConsParam = new Reserva();
	private List<Reserva> listReservas = new ArrayList<Reserva>();

	public void cadastrar() {
		logger.debug("Tentando realizar o cadastro de reserva da máquina " + reserva.getMaquina().getCodigo() + " para o cliente " + reserva.getCliente().getNomeFantasia());
		
		try {
			reservaService.cadastrar(reserva);
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reserva da máquina " + reserva.getMaquina().getCodigo() + " para o cliente " + reserva.getCliente().getNomeFantasia() + " cadastrada com sucesso.", null));
			logger.info("Reserva da máquina " + reserva.getMaquina().getCodigo() + " para o cliente " + reserva.getCliente().getNomeFantasia() + " cadastrada com sucesso.");
			
			// Processo de auditoria de cadastro de usuário
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Cadastro");
			auditoria.setDescricao("Cadastrou uma reserva para a máquina " + reserva.getMaquina().getCodigo() + " para o cliente " + reserva.getCliente().getNomeFantasia());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
		}
		
		reserva = new Reserva(); 
	}
	
	public void consultar(boolean exibirMensagem) {
		try {
			listReservas = reservaService.buscarComFiltro(reservaConsParam);
		} catch (CadastroInexistenteException e) {
			if(listReservas != null && !listReservas.isEmpty()) {
				listReservas.clear();
			}
			
			if(exibirMensagem) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			}
		}
	}
	
	public void excluir() {
		logger.debug("Tentando realizar a exclusão da reserva da máquina " + reserva.getMaquina().getCodigo() + " para o cliente " + reserva.getCliente().getNomeFantasia());
		
		try {
			reserva = reservaService.excluir(reserva.getId());
			
			// Sucesso - Exibe mensagem de cadastro realizado com sucesso
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reserva da máquina " + reserva.getMaquina().getCodigo() + " para o cliente " + reserva.getCliente().getNomeFantasia() + " excluída com sucesso.", null));
			logger.info("Reserva da máquina " + reserva.getMaquina().getCodigo() + " para o cliente " + reserva.getCliente().getNomeFantasia() + " excluída com sucesso.");
			
			// Processo de auditoria de cadastro de usuário
			Auditoria auditoria = new Auditoria();
			auditoria.setDataAcao(new Date());
			auditoria.setTitulo("Exclusão");
			auditoria.setDescricao("Excluiu uma reserva para a máquina " + reserva.getMaquina().getCodigo() + " para o cliente " + reserva.getCliente().getNomeFantasia());
			auditoria.setUsuario(Seguranca.getUsuarioLogado());
			auditoria.setIp(request.getRemoteAddr());
			auditoriaService.cadastrarNovaAcao(auditoria);
		} catch (InconsistenciaException e) {
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			logger.info(e.getMessage());
			return;
		}
		// Recarrega a listagem de atividades
		consultar(false);
	}
	
	public void selecionarCliente(String codigo) {
		Cliente c = clienteService.findByCodigo(codigo);
		
		if(c != null) {
			if(!c.getIndAtivo()) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código inválido: O código " + codigo + " informado não corresponde a um cliente ativo.", null));
				reserva.setCliente(new Cliente());
				return;
			}
			reserva.setCliente(c);
		} else {
			reserva.setCliente(new Cliente());
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código inválido: O código " + codigo + " informado não corresponde a nenhum cliente cadastrado no sistema.", null));
			return;
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaCliente').hide();");
	}
	
	public void selecionarMaquina(String codigo) {
		Maquina maq = maquinaService.findByCodigo(codigo);
		
		if(maq != null) {
			if(!"EM ESTOQUE".equalsIgnoreCase(maq.getMaquinaStatus().getDescricao())) {
				ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código inválido: O código " + codigo + " informado não corresponde a uma máquina disponível em estoque.", null));
				reserva.setMaquina(new Maquina());
				return;
			}
			reserva.setMaquina(maq);
		} else{
			reserva.setMaquina(new Maquina());
			ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código inválido: O código " + codigo + " informado não corresponde a nenhuma máquina cadastrada no sistema.", null));
			return;
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgConsultaMaquina').hide();");
	}
	
	public void consultarCliente() {
		try {
			// Busca os clientes ativos
			cliente.setIndAtivo(true);
			listClientes = clienteService.buscarClientesComFiltro(cliente);
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaCliente').show();");
		} catch (CadastroInexistenteException e) {
			listMaquinas.clear();
			ctx.addMessage("Message2", new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaCliente').show();");
		}
	}
	
	public void consultarMaquina() {
		try {
			List<String> listMaquinaStatus = Arrays.asList("EM ESTOQUE");
			listMaquinas = maquinaService.buscarComFiltroComVariosStatus(maquina, listMaquinaStatus);
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaMaquina').show();");
		} catch (CadastroInexistenteException e) {
			listMaquinas.clear();
			ctx.addMessage("Message2", new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgConsultaMaquina').show();");
		}
	}
	
	public void abrirDialog(String dialog) {
		listMaquinas = new ArrayList<Maquina>();
		maquina = new Maquina();
		
		listClientes = new ArrayList<Cliente>();
		cliente = new Cliente();
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('" + dialog + "').show();");
	}
	
	public void fecharDialog(String dialog) {
		listMaquinas.clear();
		listClientes.clear();
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('" + dialog + "').hide();");
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Maquina getMaquina() {
		return maquina;
	}

	public void setMaquina(Maquina maquina) {
		this.maquina = maquina;
	}

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	public List<Maquina> getListMaquinas() {
		return listMaquinas;
	}

	public void setListMaquinas(List<Maquina> listMaquinas) {
		this.listMaquinas = listMaquinas;
	}

	public List<Cliente> getListClientes() {
		return listClientes;
	}

	public void setListClientes(List<Cliente> listClientes) {
		this.listClientes = listClientes;
	}

	public Reserva getReservaConsParam() {
		return reservaConsParam;
	}

	public void setReservaConsParam(Reserva reservaConsParam) {
		this.reservaConsParam = reservaConsParam;
	}

	public List<Reserva> getListReservas() {
		return listReservas;
	}

	public void setListReservas(List<Reserva> listReservas) {
		this.listReservas = listReservas;
	}
}
