package mx.com.webtrack.qbo.geofence.mb;


import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import mx.com.webtrack.qbo.geofence.bo.GeofenceMigrateBo;
import mx.com.webtrack.qbo.geofence.vo.GeofenceMigrateVo;
import mx.com.webtrack.qbo.geofence.vo.GeofenceMigrateUsersVo;
import mx.com.webtrack.qbo.login.util.LoginConstants;
import mx.com.webtrack.qbo.to.GeofenceUser;
import mx.com.webtrack.qbo.to.Geofences;
import mx.com.webtrack.qbo.to.Usuario;
import mx.com.webtrack.qbo.util.Utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@SuppressWarnings("deprecation")
@ManagedBean(name = "geofenceMigrateMb")
@ViewScoped

public class GeofenceMigrateMb implements Serializable{

	private static final long serialVersionUID = -8200134945673700076L;

	@Autowired
	GeofenceMigrateBo geofenceMigrateBo;
	
	int geofenceSelectedId;
	private boolean renderUserCheckBox;
	private List<Geofences> geofences;
	private List<GeofenceMigrateVo> comboGeofences;
	private GeofenceMigrateVo newComboGeofences;
	private GeofenceMigrateVo geofenceSelected;
	private Usuario usuario;
	private List<GeofenceMigrateUsersVo> usersMigrate;
	private List<Usuario> usuariosFiltrados;
	private List<GeofenceMigrateUsersVo> comboUsers;
	private Integer idUsuarioSelect;
	private String nombreCliente;
	private Calendar fecha;
	private Map<Integer, Map<Integer, GeofenceMigrateUsersVo>> geofenceUserMap;
	
	public GeofenceMigrateMb(){
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);		
		
		Usuario user = (Usuario) session.getAttribute(LoginConstants.SESSION_USER);
		usuario = user;
		geofences = geofenceMigrateBo.getGeofencesByUser(user);	
		geofenceUserMap = new HashMap<Integer, Map<Integer, GeofenceMigrateUsersVo>>();
		nombreCliente = user.getNombre() +" " + user.getApellidoPaterno()+ " " + user.getApellidoMaterno() + "-" + user.getPerfil().getCliente().getNombre();
		cargarGeocercas();
	}
	
	public void selectGeofence(int geofenceId){
		comboUsers = new ArrayList<GeofenceMigrateUsersVo>();
		FacesContext.getCurrentInstance().getAttributes().get(comboGeofences);
		geofenceSelected = null;
		renderUserCheckBox = false;
		for(GeofenceMigrateVo geofenceMigrate: comboGeofences) {
			geofenceMigrate.setStatus(false);
			if(geofenceMigrate.getId() == geofenceId){
				geofenceSelected = geofenceMigrate;
				geofenceMigrate.setStatus(true);
			}
			updateForm();
		}
		updateForm();
		if(geofenceSelected != null){
			cargarUsuarios();
			renderUserCheckBox = true;
		}
	}
	
	public void selectUser(){
		updateGeofencesUserMap(true);
		updateForm();
	}
	
	private void updateGeofencesUserMap(boolean saving) {
		for(GeofenceMigrateVo geofenceMigrate: comboGeofences) {
			if(geofenceMigrate.getStatus()){
				geofenceSelected = geofenceMigrate;
			}
		}
		if(geofenceSelected != null){
			Map<Integer, GeofenceMigrateUsersVo> userGeofenceMap = geofenceUserMap.get(geofenceSelected.getId());
			if(userGeofenceMap == null){
				userGeofenceMap = new HashMap<Integer, GeofenceMigrateUsersVo>();
			}
			//System.out.println("GEOFENCE: " + geofenceSelected.getId() + "OWNER" + geofenceSelected.getIdUser());
			for(GeofenceMigrateUsersVo user: comboUsers){
				//System.out.println("GEOFENCE: " + geofenceSelected.getId() + " User: " + user.getIdUsuario() + "  enable:" + user.getAsignar());
				if(user.getAsignar()){
					userGeofenceMap.put(user.getIdUsuario(), user);
					//System.out.println("ADD");
				}
				else{
					userGeofenceMap.remove(user.getIdUsuario());
				}
			}
			if(saving){
				geofenceUserMap.put(geofenceSelected.getId(), userGeofenceMap);
			}
		}
		updateForm();
	}
	
	public void updateForm(){
		RequestContext context = RequestContext.getCurrentInstance();
		context.update("geofenceMigrateFrm");
	}

	public  void cargarUsuarios(){
		List<GeofenceMigrateUsersVo> lista = new ArrayList<GeofenceMigrateUsersVo>();
		List<GeofenceUser> geofenceUserList = new ArrayList<GeofenceUser>();
		Map<Integer, GeofenceMigrateUsersVo> userGeofenceMap = geofenceUserMap.get(geofenceSelected.getId());
		if(userGeofenceMap == null){
			userGeofenceMap = new HashMap<Integer, GeofenceMigrateUsersVo>();
		}
		usersMigrate =	geofenceMigrateBo.getUsers(usuario.getIdUsuario(), geofenceSelected.getId(), geofenceSelected.getName(), usuario.getPerfil().getCliente().getIdCliente());
		geofenceUserList = geofenceMigrateBo.getGeofenceUser(geofenceSelected.getId());
		for (GeofenceMigrateUsersVo user: usersMigrate){
			//System.out.println("GeoFence:" + geofenceSelected.getId() + " Owner: " + geofenceSelected.getIdUser() + " User:" + user.getIdUsuario());
			if(geofenceSelected.getIdUser() == user.getIdUsuario()){
				user.setAsignar(true);
				user.setRenderCheckBox(false);
				//System.out.println("::::OWNER::::");
			}
			else{
				user.setRenderCheckBox(true);
				user.setAsignar(false);
				for(GeofenceUser geofenceUser: geofenceUserList){
					//System.out.println("geofenceUser.geofence:" + geofenceUser.getGeofences().getId() + " geofenceSelected:" + geofenceSelected.getId() + " geofenceUser.user:" + geofenceUser.getUsuario().getIdUsuario() + "  user:" + user.getIdUsuario());
					if(userGeofenceMap.isEmpty()){
						if((geofenceUser.getGeofences().getId().intValue() == geofenceSelected.getId().intValue()) && (geofenceUser.getUsuario().getIdUsuario().intValue() == user.getIdUsuario().intValue())){
							user.setAsignar(true);
							//System.out.println("selected user from DB");
						}
					}
					else{
						if(userGeofenceMap.containsKey(user.getIdUsuario())){
							user.setAsignar(true);
							//System.out.println("selected user from MAP");
						}
					}
				}
				//System.out.println("::::NOT OWNER::::");
			}
			lista.add(user);
		}
		comboUsers = lista;
		updateGeofencesUserMap(true);
		updateForm();
	}
	
	public void cargarGeocercas(){
		List<GeofenceMigrateVo> lista = new ArrayList<GeofenceMigrateVo>();
		for(Geofences geofence: geofences){
			GeofenceMigrateVo th = new GeofenceMigrateVo(geofence);
			th.setStatus(false);
			lista.add(th);				
		}
		comboGeofences = lista;
		Collections.sort(comboGeofences);
	}
		
	public void save(ActionEvent e){
		for(GeofenceMigrateVo geofenceMigrate: comboGeofences) {
			if(geofenceMigrate.getStatus() == true){
				geofenceSelected = geofenceMigrate;
				break;
			}
		}
		
		Map<Integer, List<GeofenceUser>> geofencesToUpdateMap = new HashMap<Integer, List<GeofenceUser>>();
		List<GeofenceUser> geofenceUserList;
		Map<Integer, GeofenceMigrateUsersVo> gmuMap;
		GeofenceMigrateUsersVo user;
		GeofenceUser geofenceUser;
		Geofences geofence;
		Usuario usuarioTmp;
		for(Integer geofenceId: geofenceUserMap.keySet()){
			gmuMap = geofenceUserMap.get(geofenceId);
			//System.out.println("geofence: " + geofenceId);
			geofenceUserList = new ArrayList<GeofenceUser>();
			for(Integer userId: gmuMap.keySet()){
				user = gmuMap.get(userId);
				//System.out.println("geofence: " + geofenceId + " User : " + user.getIdUsuario() + "  enable:" + user.getAsignar());
				if(user.getAsignar()){
					//System.out.println("add");
					geofence = new Geofences();
					geofence.setId(geofenceId);
					usuarioTmp = new Usuario();
					usuarioTmp.setIdUsuario(userId);
					geofenceUser = new GeofenceUser(geofence, usuarioTmp);
					geofenceUserList.add(geofenceUser);
				}
			}
			geofencesToUpdateMap.put(geofenceId, geofenceUserList);
		}
		if(!geofencesToUpdateMap.isEmpty()){
			geofenceUserMap = new HashMap<Integer, Map<Integer,GeofenceMigrateUsersVo>>();
		}
		geofenceMigrateBo.saveGeofenceUser(geofencesToUpdateMap);
		geofenceSelected = null;
		geofences = geofenceMigrateBo.getGeofencesByUser(usuario);
		cargarGeocercas();
		comboUsers = new ArrayList<GeofenceMigrateUsersVo>();
	}
	
	public boolean validatesRequiredField(){
		boolean error = false;
		int countGeofences = 0;
		int countUSers = 0;
		for (int i = 0; i < comboGeofences.size(); i++) {
			if(comboGeofences.get(i).getStatus() == true)
				countGeofences++;
		}
		if(countGeofences < 1){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,Utils.getResourceText("lbl_geofence_migrate_error1"),Utils.getResourceText("lbl_geofence_migrate_error4_geocercas")));
			comboUsers = new ArrayList<GeofenceMigrateUsersVo>();
			error = true;
		}		
		
		countUSers = 0;
		for (int i = 0; i < comboUsers.size(); i++) {
			if(comboUsers.get(i).getAsignar() == true)
				countUSers++;
		}
		if(countUSers < 1 && countGeofences == 1 ){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,Utils.getResourceText("lbl_geofence_migrate_error1"),Utils.getResourceText("lbl_geofence_migrate_error3_usuarios")));
			error = true;
		}		
		return error;	
	}
	
public void preExportExcel(Object document){
		HSSFWorkbook wb = (HSSFWorkbook) document;
		wb.setSheetName(0, "Reporte-"+ nombreCliente);
		HSSFSheet sheet = wb.getSheetAt(0);
		sheet.setDisplayGridlines(false);
	}

	public void postExportExcel(Object document){
		short border = HSSFCellStyle.BORDER_THIN;
		int titulo = 1;
		int desp = 6;
		int tituloTabla = 0;
		int tabla = 1;
		
		HSSFWorkbook wb = (HSSFWorkbook) document;
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFCellStyle cellStyleTitulo = wb.createCellStyle();
		HSSFCellStyle cellStyleCliente = wb.createCellStyle();
		HSSFFont fontStyle = wb.createFont();
		HSSFFont fontStyleTitulo = wb.createFont();
		HSSFFont fontStyleCliente = wb.createFont();
		for (int i = sheet.getPhysicalNumberOfRows(); i  >= 1 ; i--) {
			sheet.shiftRows(i, i, desp);
		}		
		sheet.shiftRows(sheet.getFirstRowNum(), sheet.getFirstRowNum(), desp-1);
		HSSFRow  row = sheet.createRow(tituloTabla + desp);
        cellStyle.setBorderLeft(border);
		cellStyle.setBorderRight(border);
        cellStyle.setBorderTop(border);
        cellStyle.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); 
		for (int i = 0; i <= 5; i++) {
			row.createCell(i);
			row.getCell(i).setCellStyle(cellStyle);
		}
		row  = sheet.getRow(tituloTabla + desp - 1);
		fontStyle.setColor(HSSFColor.WHITE.index);
		fontStyle.setFontHeightInPoints((short) 10);
		fontStyle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		cellStyle.setFont(fontStyle);
		cellStyle.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setBorderBottom(border);
		cellStyle.setBorderLeft(border);
		cellStyle.setBorderRight(border);
		for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
			row.getCell(i).setCellStyle(cellStyle);
		}
		for (int j = tabla + desp; j < comboGeofences.size() + tabla + desp; j++) {
			row  = sheet.getRow(j);
			cellStyle = wb.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.TAN.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			cellStyle.setBorderBottom(border);
			cellStyle.setBorderLeft(border);
			cellStyle.setBorderRight(border);
			cellStyle.setBorderTop(border);
			for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
				row.getCell(i).setCellStyle(cellStyle);
			}
		}
		
		row  = sheet.createRow(titulo);
		HSSFCell cell =row.createCell(0);
        fontStyleTitulo.setColor(HSSFColor.ROYAL_BLUE.index);
		fontStyleTitulo.setFontHeightInPoints((short) 18);
		fontStyleTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		cellStyleTitulo.setFillForegroundColor(HSSFColor.WHITE.index);
		cellStyleTitulo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyleTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);;
		cellStyleTitulo.setFont(fontStyleTitulo);
		cell.setCellValue(new HSSFRichTextString(Utils.getResourceText("lbl_geofence_migrate_titulo_reporte")));
		cell.setCellStyle(cellStyleTitulo);
		sheet.addMergedRegion(new Region(1,(short)0,1,(short)3));
		
		row  = sheet.createRow(3);
		HSSFCell cellCliente =row.createCell(0);
        fontStyleCliente.setColor(HSSFColor.BLACK.index);
		fontStyleCliente.setFontHeightInPoints((short) 12);
		cellStyleCliente.setFillForegroundColor(HSSFColor.WHITE.index);
		cellStyleCliente.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyleCliente.setAlignment(HSSFCellStyle.ALIGN_CENTER);;
		cellStyleCliente.setFont(fontStyleCliente);
		cellCliente.setCellValue(new HSSFRichTextString(Utils.getResourceText("lbl_geofence_migrate_nombre_usuario") + " : " + new HSSFRichTextString(nombreCliente)));
		cellCliente.setCellStyle(cellStyleCliente);
		cellCliente = row.createCell(1); 
		sheet.addMergedRegion(new Region(3,(short)0,3,(short)2));
		
		cellCliente = row.createCell(3);
		TimeZone tz = TimeZone.getTimeZone("GMT-00");
		fecha = Calendar.getInstance(tz);
		DateFormat formater = DateFormat.getDateTimeInstance();
		formater.setTimeZone(TimeZone.getTimeZone("GMT-06"));
		cellCliente.setCellValue(new HSSFRichTextString(Utils.getResourceText("lbl_geofence_migrate_fecha") + " : " + formater.format(fecha.getTime())));
		cellCliente.setCellStyle(cellStyleCliente);
		sheet.addMergedRegion(new Region(3,(short)3,3,(short)3));
		for (int i = 0; i <= 5; i++) {
			sheet.autoSizeColumn(i);
			sheet.addMergedRegion(new Region(tituloTabla - 1 + desp,(short)i,tituloTabla + desp,(short)i));
		}
	}

	public void selectAll(ActionEvent actionEvent) {
		geofenceSelected = null;
		for(GeofenceMigrateVo geofenceMigrate: comboGeofences) {
			if(geofenceMigrate.getStatus() == true){
				geofenceSelected = geofenceMigrate;
			}
		}
		if(geofenceSelected != null){
			for(GeofenceMigrateUsersVo geofenceMigrateUser: comboUsers){
				geofenceMigrateUser.setAsignar(true);
			}
		}
		else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,Utils.getResourceText("lbl_geofence_migrate_error4_geocercas"),Utils.getResourceText("lbl_map_error_no_data_found_description")));
		}
		updateGeofencesUserMap(true);
	}
	
	public void deselectAll(ActionEvent actionEvent) {
		geofenceSelected = null;
		for(GeofenceMigrateVo geofenceMigrate: comboGeofences) {
			if(geofenceMigrate.getStatus() == true){
				geofenceSelected = geofenceMigrate;
			}
		}
		if(geofenceSelected != null){
			for (GeofenceMigrateUsersVo geofenceMigrateUser: comboUsers) {
				if(geofenceSelected.getIdUser() != geofenceMigrateUser.getIdUsuario()){
					geofenceMigrateUser.setAsignar(false);
				}
			}
		}
		else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,Utils.getResourceText("lbl_geofence_migrate_error4_geocercas"),Utils.getResourceText("lbl_map_error_no_data_found_description")));
		}
		updateGeofencesUserMap(true);
	}
	
	public int getGeofenceSelectedId() {
		return geofenceSelectedId;
	}
	public void setGeofenceSelectedId(int geofenceSelectedId) {
		this.geofenceSelectedId = geofenceSelectedId;
	}
	public boolean isRenderUserCheckBox() {
		return renderUserCheckBox;
	}
	public void setRenderUserCheckBox(boolean renderUserCheckBox) {
		this.renderUserCheckBox = renderUserCheckBox;
	}
	public List<Geofences> getGeofences() {
		return geofences;
	}
	public void setGeofences(List<Geofences> geofences) {
		this.geofences = geofences;
	}
	public List<Usuario> getUsuariosFiltrados() {
		return usuariosFiltrados;
	}
	public void setUsuariosFiltrados(List<Usuario> usuariosFiltrados) {
		this.usuariosFiltrados = usuariosFiltrados;
	}
	public List<GeofenceMigrateUsersVo> getComboUsers() {
		return comboUsers;
	}
	public void setComboUsers(List<GeofenceMigrateUsersVo> comboUsers) {
		this.comboUsers = comboUsers;
	}
	public Integer getIdUsuarioSelect() {
		return idUsuarioSelect;
	}
	public void setIdUsuarioSelect(Integer idUsuarioSelect) {
		this.idUsuarioSelect = idUsuarioSelect;
	}
	public List<GeofenceMigrateVo> getComboGeofences() {
		return comboGeofences;
	}
	public void setComboGeofences(List<GeofenceMigrateVo> comboGeofences) {
		this.comboGeofences = comboGeofences;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public GeofenceMigrateVo getGeofenceSelected() {
		return geofenceSelected;
	}
	public void setGeofenceSelected(GeofenceMigrateVo geofenceSelected) {
		this.geofenceSelected = geofenceSelected;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public GeofenceMigrateVo getNewComboGeofences() {
		return newComboGeofences;
	}
	public void setNewComboGeofences(GeofenceMigrateVo newComboGeofences) {
		this.newComboGeofences = newComboGeofences;
	}
}

