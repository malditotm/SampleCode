package mx.com.webtrack.qbo.to;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Usuario entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "Usuario", schema = "dbo",  uniqueConstraints = @UniqueConstraint(columnNames = "usuario"))
public class Usuario implements java.io.Serializable {
	private static final long serialVersionUID = -2236084718840856915L;

	// Fields
	private Integer idUsuario;
	private Idioma idioma;
	private Perfil perfil;
	private Theme theme;
	private Estatus estatus;
	private String usuario;
	private String contrasena;
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private Integer diasCambioPsw;
	private String correo;
	private Boolean evaluo;
	private Set<LocalizadorAvisoPanico> localizadorAvisoPanicos = new HashSet<LocalizadorAvisoPanico>(
			0);
	private Set<UsuarioTipoMarcador> usuarioTipoMarcadors = new HashSet<UsuarioTipoMarcador>(
			0);
	private Set<UsuarioGrupoVehiculo> usuarioGrupoVehiculos = new HashSet<UsuarioGrupoVehiculo>(
			0);
	private Set<HorarioUsuario> horarioUsuarios = new HashSet<HorarioUsuario>(0);
	private Set<UsuarioDispositivoSesion> usuarioDispositivoSesions = new HashSet<UsuarioDispositivoSesion>(
			0);
	//private Set<GimMaquina> gimMaquinas = new HashSet<GimMaquina>(0);
	private Set<Contrato> contratos = new HashSet<Contrato>(0);
	private Set<InformeProgramacion> informeProgramacions = new HashSet<InformeProgramacion>(
			0);
	private Set<EmailReasonCodes> emailReasonCodeses = new HashSet<EmailReasonCodes>(
			0);
	private Set<HistoricoUsuarioBloqueado> historicoUsuarioBloqueados = new HashSet<HistoricoUsuarioBloqueado>(
			0);
	private Set<IntentoIngreso> intentoIngresos = new HashSet<IntentoIngreso>(0);
	private Set<UsuarioVehiculo> usuarioVehiculos = new HashSet<UsuarioVehiculo>(
			0);
	private Set<Notificacion> notificacions = new HashSet<Notificacion>(0);
	private Set<DispositivoMovil> dispositivoMovils = new HashSet<DispositivoMovil>(
			0);
	private Set<ModoRobo> modoRobos = new HashSet<ModoRobo>(0);

	// Constructors

	/** default constructor */
	public Usuario() {
	}

	/** minimal constructor */
	public Usuario(Perfil perfil, String usuario, String nombre) {
		this.perfil = perfil;
		this.usuario = usuario;
		this.nombre = nombre;
	}
	
	/** Basic constructor to create new Users 
	 * 	@param usuario  Username to assign to the new user
	 *  @param nombre  Real name of the user
	 *	@param apellidoPaterno  First name of the user
	 *	@param apellidoMaterno  Mother's maiden name of the user
	 *	@param contrasena  Password to assign to the new user generate with // TODO
	 *	@param correo  Email to assign to the new user
	 *	@param idioma  Language to assign to the new user
	 *	@param perfil  Profile to assign to the new user
	 *	@param theme  Visual theme to assign to the new user if null, on first login the system will assign the default theme
	 *	@param estatus  Status of the user in the system, by default set 9 to say that is its 1st login
	 *	@param diasCambioPsw  Days for the user to use the same password
	 * **/
	public Usuario(String usuario, String nombre, String apellidoPaterno, String apellidoMaterno, String contrasena, String correo,  Idioma idioma, Perfil perfil, Theme theme, Estatus estatus, Integer diasCambioPsw){
		this.usuario = usuario;
		this.nombre = nombre;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.contrasena = contrasena;
		this.correo = correo;
		this.idioma = idioma;
		this.perfil = perfil;
		this.theme = theme;
		this.estatus = estatus;
		this.diasCambioPsw = diasCambioPsw;
	}

	
	// Property accessors
	//@GenericGenerator(name = "generator", strategy = "hilo.long")
	@Id
	@GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
	@Column(name = "idUsuario", unique = true, nullable = false)
	public Integer getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idIdioma")
	public Idioma getIdioma() {
		return this.idioma;
	}

	public void setIdioma(Idioma idioma) {
		this.idioma = idioma;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPerfil", nullable = false)
	public Perfil getPerfil() {
		return this.perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idTheme")
	public Theme getTheme() {
		return this.theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idEstatus")
	public Estatus getEstatus() {
		return this.estatus;
	}

	public void setEstatus(Estatus estatus) {
		this.estatus = estatus;
	}

	@Column(name = "usuario", unique = true, nullable = false, length = 50)
	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Column(name = "contrasena", length = 50)
	public String getContrasena() {
		return this.contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	@Column(name = "nombre", nullable = false, length = 200)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "apellidoPaterno", length = 200)
	public String getApellidoPaterno() {
		return this.apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	@Column(name = "apellidoMaterno", length = 200)
	public String getApellidoMaterno() {
		return this.apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	@Column(name = "diasCambioPsw")
	public Integer getDiasCambioPsw() {
		return this.diasCambioPsw;
	}

	public void setDiasCambioPsw(Integer diasCambioPsw) {
		this.diasCambioPsw = diasCambioPsw;
	}

	@Column(name = "correo", length = 200)
	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	@Column(name = "evaluo")
	public Boolean getEvaluo() {
		return this.evaluo;
	}

	public void setEvaluo(Boolean evaluo) {
		this.evaluo = evaluo;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<LocalizadorAvisoPanico> getLocalizadorAvisoPanicos() {
		return this.localizadorAvisoPanicos;
	}

	public void setLocalizadorAvisoPanicos(
			Set<LocalizadorAvisoPanico> localizadorAvisoPanicos) {
		this.localizadorAvisoPanicos = localizadorAvisoPanicos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<UsuarioTipoMarcador> getUsuarioTipoMarcadors() {
		return this.usuarioTipoMarcadors;
	}

	public void setUsuarioTipoMarcadors(
			Set<UsuarioTipoMarcador> usuarioTipoMarcadors) {
		this.usuarioTipoMarcadors = usuarioTipoMarcadors;
	}

	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<UsuarioGrupoVehiculo> getUsuarioGrupoVehiculos() {
		return this.usuarioGrupoVehiculos;
	}

	public void setUsuarioGrupoVehiculos(
			Set<UsuarioGrupoVehiculo> usuarioGrupoVehiculos) {
		this.usuarioGrupoVehiculos = usuarioGrupoVehiculos;
	}

	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<HorarioUsuario> getHorarioUsuarios() {
		return this.horarioUsuarios;
	}

	public void setHorarioUsuarios(Set<HorarioUsuario> horarioUsuarios) {
		this.horarioUsuarios = horarioUsuarios;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<UsuarioDispositivoSesion> getUsuarioDispositivoSesions() {
		return this.usuarioDispositivoSesions;
	}

	public void setUsuarioDispositivoSesions(
			Set<UsuarioDispositivoSesion> usuarioDispositivoSesions) {
		this.usuarioDispositivoSesions = usuarioDispositivoSesions;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<Contrato> getContratos() {
		return this.contratos;
	}

	public void setContratos(Set<Contrato> contratos) {
		this.contratos = contratos;
	}


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<InformeProgramacion> getInformeProgramacions() {
		return this.informeProgramacions;
	}

	public void setInformeProgramacions(
			Set<InformeProgramacion> informeProgramacions) {
		this.informeProgramacions = informeProgramacions;
	}


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<EmailReasonCodes> getEmailReasonCodeses() {
		return this.emailReasonCodeses;
	}

	public void setEmailReasonCodeses(Set<EmailReasonCodes> emailReasonCodeses) {
		this.emailReasonCodeses = emailReasonCodeses;
	}

	

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<HistoricoUsuarioBloqueado> getHistoricoUsuarioBloqueados() {
		return this.historicoUsuarioBloqueados;
	}

	public void setHistoricoUsuarioBloqueados(
			Set<HistoricoUsuarioBloqueado> historicoUsuarioBloqueados) {
		this.historicoUsuarioBloqueados = historicoUsuarioBloqueados;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<IntentoIngreso> getIntentoIngresos() {
		return this.intentoIngresos;
	}

	public void setIntentoIngresos(Set<IntentoIngreso> intentoIngresos) {
		this.intentoIngresos = intentoIngresos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<UsuarioVehiculo> getUsuarioVehiculos() {
		return this.usuarioVehiculos;
	}

	public void setUsuarioVehiculos(Set<UsuarioVehiculo> usuarioVehiculos) {
		this.usuarioVehiculos = usuarioVehiculos;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<Notificacion> getNotificacions() {
		return this.notificacions;
	}

	public void setNotificacions(Set<Notificacion> notificacions) {
		this.notificacions = notificacions;
	}

	

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<DispositivoMovil> getDispositivoMovils() {
		return this.dispositivoMovils;
	}

	public void setDispositivoMovils(Set<DispositivoMovil> dispositivoMovils) {
		this.dispositivoMovils = dispositivoMovils;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usuario")
	public Set<ModoRobo> getModoRobos() {
		return this.modoRobos;
	}

	public void setModoRobos(Set<ModoRobo> modoRobos) {
		this.modoRobos = modoRobos;
	}

}