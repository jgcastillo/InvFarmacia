package org.farmacia.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.farmacia.fxmodel.MedicamentoFx;
import org.farmacia.fxmodel.UsuarioFx;
import org.farmacia.jpacontroller.ComponenteFacade;
import org.farmacia.jpacontroller.EquivalenteFacade;
import org.farmacia.jpacontroller.InventarioFacade;
import org.farmacia.jpacontroller.MedicamentoFacade;
import org.farmacia.jpacontroller.MedicamentoHasComponenteFacade;
import org.farmacia.jpacontroller.MedicamentoHasEquivalenteFacade;
import org.farmacia.jpacontroller.MedicamentoHasPresentacionFacade;
import org.farmacia.jpacontroller.MedicamentoHasUsoFacade;
import org.farmacia.jpacontroller.MovimientoFacade;
import org.farmacia.jpacontroller.PresentacionFacade;
import org.farmacia.jpacontroller.UsoFacade;
import org.farmacia.jpacontroller.UsuarioFacade;
import org.farmacia.jpamodel.Componente;
import org.farmacia.jpamodel.Equivalente;
import org.farmacia.jpamodel.Inventario;
import org.farmacia.jpamodel.Medicamento;
import org.farmacia.jpamodel.MedicamentoHasComponente;
import org.farmacia.jpamodel.MedicamentoHasEquivalente;
import org.farmacia.jpamodel.MedicamentoHasPresentacion;
import org.farmacia.jpamodel.MedicamentoHasUso;
import org.farmacia.jpamodel.Movimiento;
import org.farmacia.jpamodel.Presentacion;
import org.farmacia.jpamodel.Uso;
import org.farmacia.jpamodel.Usuario;
import org.farmacia.utils.SecurityUtil;

/**
 * Esta clase recibe las propiedades de JavaFX y las comuica con los objetos
 * entities, para su tratameiento en el back end. Funciona a manera de
 * MiddleWare.
 *
 * @author jgcastillo
 */
public class MiddlewareController {

    // Clase manejadoras de la base de dato
    private final MedicamentoFacade medicamentoFacade;
    private final UsoFacade usoFacade;
    private final MedicamentoHasUsoFacade medHUsoFacade;
    private final PresentacionFacade presentacionFacade;
    private final MedicamentoHasPresentacionFacade medHPreFacade;
    private final MedicamentoHasEquivalenteFacade medHEquiFacade;
    private final ComponenteFacade componenteFacade;
    private final MedicamentoHasComponenteFacade medHCompFacade;
    private final InventarioFacade inventarioFacade;
    private final MovimientoFacade movimientoFacade;
    private final UsuarioFacade usuarioFacade;
    private final EquivalenteFacade equivalenteFacade;

    // Clase de utilidades
    private final SecurityUtil secureUtil;
    private static final Logger LOG = Logger.getLogger(MiddlewareController.class.getName());

    public MiddlewareController() {
        this.medicamentoFacade = new MedicamentoFacade();
        this.usoFacade = new UsoFacade();
        this.medHUsoFacade = new MedicamentoHasUsoFacade();
        this.presentacionFacade = new PresentacionFacade();
        this.medHPreFacade = new MedicamentoHasPresentacionFacade();
        this.componenteFacade = new ComponenteFacade();
        this.medHCompFacade = new MedicamentoHasComponenteFacade();
        this.medHEquiFacade = new MedicamentoHasEquivalenteFacade();
        this.inventarioFacade = new InventarioFacade();
        this.movimientoFacade = new MovimientoFacade();
        this.usuarioFacade = new UsuarioFacade();
        this.equivalenteFacade = new EquivalenteFacade();
        this.secureUtil = new SecurityUtil();
    }

    public void saveMedicamento(MedicamentoFx medFx) {
        Medicamento medicamento = new Medicamento(medFx.getNombre());
        medicamento.setUbicacion(medFx.getUbicacion());
        
        List<String> usosStrs = medFx.getUsos();
        List<String> compStrs = medFx.getComponentes();
        List<String> equiStrs = medFx.getEquivalentes();

        // se crea el medicamento en DB
        medicamento = medicamentoFacade.create(medicamento);

        // se crea la presentacion y se asocia al medicamento
        Optional<Presentacion> optPresenta = presentacionFacade.findByNombre(medFx.getPresentacion());
        Presentacion presentacion;
        if (optPresenta.isPresent()) {
            presentacion = optPresenta.get();
        } else {
            presentacion = new Presentacion(medFx.getPresentacion());
            presentacion = presentacionFacade.create(presentacion);
        }
        MedicamentoHasPresentacion mhp = new MedicamentoHasPresentacion();
        mhp.setMedicamentoId(medicamento);
        mhp.setPresentacionId(presentacion);
        medHPreFacade.create(mhp);
        
        // se crean los usos y se asocia al medicamento
        for (String usStr : usosStrs) {
            Optional<Uso> optUso = usoFacade.findByNombre(usStr);
            Uso us;
            if (optUso.isPresent()) {
                us = optUso.get();
            } else {
                us = new Uso(usStr);
                us = componenteFacade.create(us);
            }
            MedicamentoHasUso mhu = new MedicamentoHasUso();
            mhu.setUsoId(us);
            mhu.setMedicamentoId(medicamento);
            medHUsoFacade.create(mhu);
        }

        // se crean los componentes y se asocian al medicamento
        for (String cpteString : compStrs) {
            Optional<Componente> optCpte = componenteFacade.findByNombre(cpteString);
            Componente cpte;

            if (optCpte.isPresent()) {
                cpte = optCpte.get();
            } else {
                cpte = new Componente(cpteString);
                cpte = componenteFacade.create(cpte);
            }

            MedicamentoHasComponente mhc = new MedicamentoHasComponente();
            mhc.setComponenteId(cpte);
            mhc.setMedicamentoId(medicamento);
            medHCompFacade.create(mhc);
        }
        
        // se crean los equivalentes y se asocian al medicamento
        for(String optString : equiStrs){
            Optional<Equivalente> optEqui = equivalenteFacade.findByNombre(optString);
            Equivalente equiv;
            
            if(optEqui.isPresent()){
                equiv = optEqui.get();
            } else {
                equiv = new Equivalente(optString);
                equiv = equivalenteFacade.create(equiv);
            }
            
            MedicamentoHasEquivalente mhe = new MedicamentoHasEquivalente();
            mhe.setEquivalenteId(equiv);
            mhe.setMedicamentoId(medicamento);
            medHEquiFacade.create(mhe);
        }

        // se actualiza el inventario
        Inventario inventario = new Inventario();
        inventario.setMedicamentoId(medicamento);
        inventario.setCantidad(medFx.getCantidad());
        inventarioFacade.create(inventario);

        // se crea el movimiento
        Movimiento mov = new Movimiento();
        mov.setTipo(Movimiento.ENTRADA);
        mov.setCantidad(medFx.getCantidad());
        mov.setFecha(new Date());
        mov.setMedicamentoId(medicamento);
        movimientoFacade.create(mov);
    }

    public MedicamentoFx updateMedicamento(MedicamentoFx medFx, String oldPresentacion) {
        Medicamento medicamento = medicamentoFacade.find(medFx.getId());
        medicamento.setNombre(medFx.getNombre());
        medicamento.setUbicacion(medFx.getUbicacion());
        
        medicamento = medicamentoFacade.edit(medicamento);
        
        // actualiza presentación
        if (!medFx.getPresentacion().equals(oldPresentacion)) {
            updatePresentacion(medicamento, oldPresentacion, medFx.getPresentacion());
        }
        
        // actualiza usos
        updateUsos(medicamento, medFx);

        // actualiza componentes
        updateComponentes(medicamento, medFx);
        
        // actualiza equivalentes
        updateEquivalentes(medicamento, medFx);

        return medicamentoToMedicamentoFx(medicamento, medFx);
    }

    public void updatePresentacion(Medicamento med, String oldPresentacion, String newPresentacion) {
        // se busca la presentación actual
        Optional<Presentacion> optOldPresEntity = presentacionFacade.findByNombre(oldPresentacion);
        Presentacion oldPresEntity = optOldPresEntity.get();
        Optional<Presentacion> optNewPresEntity = presentacionFacade.findByNombre(newPresentacion);
        
        // se busca la relacion medicamento-presentacion existente
        Optional<MedicamentoHasPresentacion> optMhp = medHPreFacade
                .findByMedicamentoAndPresentacion(med, oldPresEntity);
        if (optMhp.isPresent()) {
            // ya tiene una presentación asignada
            MedicamentoHasPresentacion mhp = optMhp.get();
            if (optNewPresEntity.isPresent()) {
                mhp.setPresentacionId(optNewPresEntity.get());
            } else {
                Presentacion newPresEntity = new Presentacion(newPresentacion);
                newPresEntity = presentacionFacade.create(newPresEntity);
                mhp.setPresentacionId(newPresEntity);
            }
            medHPreFacade.edit(mhp);
        } else {
            // no tiene presentación se asigan una nueva
            MedicamentoHasPresentacion mhp = new MedicamentoHasPresentacion();
            if (optNewPresEntity.isPresent()) {
                mhp.setPresentacionId(optNewPresEntity.get());
            } else {
                Presentacion newPresEntity = new Presentacion(newPresentacion);
                newPresEntity = presentacionFacade.create(newPresEntity);
                mhp.setPresentacionId(newPresEntity);
            }
            mhp.setMedicamentoId(med);
            medHPreFacade.create(mhp);
        }
    }
    
    public void updateUsos(Medicamento med, MedicamentoFx medFx){
        List<String> usoList = medFx.getUsos();
        Optional<List<MedicamentoHasUso>> optMhuList = medHUsoFacade.findAllByMedicamento(med);

        if(optMhuList.isPresent()){
            // si tiene uso asignados se le eliminan
            int eliminados = medHUsoFacade.deleteAllByMedicamento(med);
            LOG.log(Level.INFO, "Eliminados {0} registros de uso", eliminados);
        } 
        
        // se verifica que exista el uso, de no existir se crea y se asocia
//        for(String usoNombre : usoList){
//            Optional<Uso> optUso = usoFacade.findByNombre(usoNombre);
//            Uso uso;
//            if(!optUso.isPresent()){
//                uso = new Uso(usoNombre);
//                uso = usoFacade.create(uso);
//            } else {
//                uso = optUso.get();
//            }
//            MedicamentoHasUso mhu = new MedicamentoHasUso();
//            mhu.setMedicamentoId(med);
//            mhu.setUsoId(uso);
//            medHUsoFacade.create(mhu);
//        }
//      este código aqui abajo es equivalente a lo de arriba:
        usoList.stream().map((usoNombre) -> {
            Optional<Uso> optUso = usoFacade.findByNombre(usoNombre);
            Uso uso;
            if (!optUso.isPresent()) {
                uso = new Uso(usoNombre);
                uso = usoFacade.create(uso);
            } else {
                uso = optUso.get();
            }
            return uso;
        }).map((uso) -> {
            MedicamentoHasUso mhu = new MedicamentoHasUso();
            mhu.setMedicamentoId(med);
            mhu.setUsoId(uso);
            return mhu;
        }).forEachOrdered((mhu) -> {
            medHUsoFacade.create(mhu);
        });
        
    }
    
    public void updateComponentes(Medicamento med, MedicamentoFx medFx) {
        List<String> compList = medFx.getComponentes();

        Optional<List<MedicamentoHasComponente>> optMhcList = medHCompFacade.findAllByMedicamento(med);
        if (optMhcList.isPresent()) {
            // si tiene compoenentes asignados se le eliminan
            int eliminados = medHCompFacade.deleteAllByMedicamento(med);
            LOG.log(Level.INFO, "Eliminados {0} registros de componentes", eliminados);
        }

        // se verifica que exista el componente, de no existir se crea y se asocia
        compList.stream().map((compNombre) -> {
            Optional<Componente> optComp = componenteFacade.findByNombre(compNombre);
            Componente comp;
            if (!optComp.isPresent()) {
                comp = new Componente(compNombre);
                comp = componenteFacade.create(comp);
            } else {
                comp = optComp.get();
            }
            return comp;
        }).map((comp) -> {
            MedicamentoHasComponente mhc = new MedicamentoHasComponente();
            mhc.setMedicamentoId(med);
            mhc.setComponenteId(comp);
            return mhc;
        }).forEachOrdered((mhc) -> {
            medHCompFacade.create(mhc);
        });
    }
    
    public void updateEquivalentes(Medicamento med, MedicamentoFx medFx) {
        List<String> equivList = medFx.getEquivalentes();

        Optional<List<MedicamentoHasEquivalente>> optMheList = medHEquiFacade.findAllByMedicamento(med);
        if (optMheList.isPresent()) {
            // si tiene compoenentes asignados se le eliminan
            int eliminados = medHEquiFacade.deleteAllByMedicamento(med);
            LOG.log(Level.INFO, "Eliminados {0} registros de equivalentes", eliminados);
        }

        // se verifica que exista el equivalente, de no existir se crea y se asocia
        equivList.stream().map((equivNombre) -> {
            Optional<Equivalente> EquivComp = equivalenteFacade.findByNombre(equivNombre);
            Equivalente equiv;
            if (!EquivComp.isPresent()) {
                equiv = new Equivalente(equivNombre);
                equiv = componenteFacade.create(equiv);
            } else {
                equiv = EquivComp.get();
            }
            return equiv;
        }).map((equiv) -> {
            MedicamentoHasEquivalente mhe = new MedicamentoHasEquivalente();
            mhe.setMedicamentoId(med);
            mhe.setEquivalenteId(equiv);
            return mhe;
        }).forEachOrdered((mhe) -> {
            medHEquiFacade.create(mhe);
        });
    }

    public void updateInventario(MedicamentoFx medFx) {
        Medicamento medicamento = medicamentoFacade.find(medFx.getId());

        Movimiento mov = new Movimiento();
        mov.setMedicamentoId(medicamento);
        mov.setTipo(Movimiento.ENTRADA);
        mov.setFecha(new Date());
        mov.setCantidad(medFx.getCantidad());
        
        if(!medFx.getUbicacion().equals(medicamento.getUbicacion())){
            medicamento.setUbicacion(medFx.getUbicacion());
            medicamentoFacade.edit(medicamento);
        }

        Inventario inv;
        Optional<Inventario> optInv = inventarioFacade.findByMedicamento(medicamento);
        if (optInv.isPresent()) {
            inv = optInv.get();
            int cantidad = medFx.getCantidad() + inv.getCantidad();
            inv.setCantidad(cantidad);
        } else {
            inv = new Inventario();
            inv.setMedicamentoId(medicamento);
            inv.setCantidad(medFx.getCantidad());
        }
        inventarioFacade.edit(inv);
        movimientoFacade.create(mov);
    }

    public void entregaInventario(int medId, int cantidad) {
        Medicamento medicamento = medicamentoFacade.find(medId);
        Inventario inv;
        Optional<Inventario> optInv = inventarioFacade.findByMedicamento(medicamento);
        if (optInv.isPresent()) {
            inv = optInv.get();
            inv.setCantidad(inv.getCantidad() - cantidad);

            Movimiento mov = new Movimiento();
            mov.setMedicamentoId(medicamento);
            mov.setTipo(Movimiento.SALIDA);
            mov.setFecha(new Date());
            mov.setCantidad(cantidad);

            inventarioFacade.edit(inv);
            movimientoFacade.create(mov);
        }
    }

    public boolean medicamentoExist(Integer medId) {
        Optional<Medicamento> medicamento = Optional.ofNullable(medicamentoFacade.find(medId));
        return medicamento.isPresent();
    }

    public MedicamentoFx findMedicamentoByNombre(String nombreMedicamento) {
        MedicamentoFx medFx = new MedicamentoFx();
        Medicamento medicamento = medicamentoFacade.findByNombre(nombreMedicamento);
        medFx.setNombre(medicamento.getNombre());

        Optional<List<MedicamentoHasUso>> optUsos = medHUsoFacade.findAllByMedicamento(medicamento);
        if (optUsos.isPresent()) {
            List<MedicamentoHasUso> usos = optUsos.get();
            StringBuilder sb = new StringBuilder();
            usos.forEach(u -> {
                sb.append(u.getUsoId().getNombreUso()).append(" ");
            });
        }

        return medFx;
    }
    
    public Optional<MedicamentoFx> findMedicamentoById(Integer medId) {
        Optional<MedicamentoFx> optMedFx = Optional.empty();
        Optional<Medicamento> optMed = Optional.ofNullable(medicamentoFacade.find(medId));
        if (optMed.isPresent()) {
            Medicamento med = optMed.get();
            
            MedicamentoFx medFx = new MedicamentoFx();
            medFx.setId(med.getId());
            medFx.setNombre(med.getNombre());
            medFx.setUbicacion(med.getUbicacion());
            
            Optional<Inventario> optInv = inventarioFacade.findByMedicamento(med);
            medFx.setCantidad(optInv.isPresent() ? optInv.get().getCantidad() : 0);
            
            Optional<MedicamentoHasPresentacion> optPresentacion 
                                            = medHPreFacade.findByMedicamento(med);
            medFx.setPresentacion(optPresentacion.isPresent()
                                    ? optPresentacion.get().getPresentacionId().getNombre() 
                                    : "");
            
            Optional<List<MedicamentoHasComponente>> optCompList = 
                                        medHCompFacade.findAllByMedicamento(med);
            ObservableList<String> componentes = FXCollections.observableArrayList();
            if(optCompList.isPresent()){
                List<MedicamentoHasComponente> mhcList = optCompList.get();
                mhcList.forEach(mhc -> {
                    componentes.add(mhc.getComponenteId().getNombre());
                });
            } 
            medFx.setComponentes(componentes);
            
            Optional<List<MedicamentoHasUso>> optUsosList = medHUsoFacade
                                                .findAllByMedicamento(med);
            ObservableList<String> usos = FXCollections.observableArrayList();
            if(optUsosList.isPresent()){
                List<MedicamentoHasUso> mhuList = optUsosList.get();
                mhuList.forEach(mhu -> {
                    usos.add(mhu.getUsoId().getNombreUso());
                });
            }
            medFx.setUsos(usos);
            
            Optional<List<MedicamentoHasEquivalente>> optEquivList = medHEquiFacade
                                                        .findAllByMedicamento(med);
            ObservableList<String> equivs = FXCollections.observableArrayList();
            if(optEquivList.isPresent()){
                List<MedicamentoHasEquivalente> mheList = optEquivList.get();
                mheList.forEach(mhe -> {
                    equivs.add(mhe.getEquivalenteId().getNombre());
                });
            }
            medFx.setEquivalentes(equivs);
            
            optMedFx = Optional.ofNullable(medFx);
        } 
        return optMedFx;
    }

    public List<String> findPresentacionByMedicamento(String nombreMedicamento) {
        Medicamento medicamento = medicamentoFacade.findByNombre(nombreMedicamento);
        Optional<List<MedicamentoHasPresentacion>> optLista = medHPreFacade
                .findAllByMedicamento(medicamento);

        List<String> presentaciones = new ArrayList<>();
        if (optLista.isPresent()) {
            List<MedicamentoHasPresentacion> lista = optLista.get();
            lista.forEach(mhp -> {
                presentaciones.add(mhp.getPresentacionId().getNombre());
            });
        }

        return presentaciones;
    }

    public List<String> findAllPresentacion() {
        List<Presentacion> presentaciones = presentacionFacade.findAll();
        return presentaciones.stream()
                .map(prs -> prs.getNombre())
                .collect(Collectors.toList());
    }

    public List<String> findComponentesByMedicamento(String nombreMedicamento) {
        Medicamento medicamento = medicamentoFacade.findByNombre(nombreMedicamento);
        Optional<List<MedicamentoHasComponente>> optLista = medHCompFacade
                .findAllByMedicamento(medicamento);

        List<String> componentes = new ArrayList<>();
        if (optLista.isPresent()) {
            List<MedicamentoHasComponente> lista = optLista.get();
            lista.forEach(mhc -> {
                componentes.add(mhc.getComponenteId().getNombre());
            });
        }
        return componentes;
    }

    public List<String> findUsosByMedicamento(String nombreMedicamento) {
        Medicamento medicamento = medicamentoFacade.findByNombre(nombreMedicamento);
        Optional<List<MedicamentoHasUso>> optLista = medHUsoFacade
                .findAllByMedicamento(medicamento);

        List<String> usos = new ArrayList<>();
        if (optLista.isPresent()) {
            List<MedicamentoHasUso> lista = optLista.get();
            lista.forEach(mhu -> {
                usos.add(mhu.getUsoId().getNombreUso());
            });
        }
        return usos;
    }

    public int findCantidadByMedicamento(MedicamentoFx medFx) {
        int cantidad = 0;
        Medicamento medicamento = medicamentoFacade.findByNombre(medFx.getNombre());
        Optional<Inventario> optInv = inventarioFacade.findByMedicamento(medicamento);
        if (optInv.isPresent()) {
            Inventario invt = optInv.get();
            cantidad = invt.getCantidad();
        }
        return cantidad;
    }

    public int findCantidadByMedicamento(Medicamento med) {
        int cantidad = 0;
        Optional<Inventario> optInv = inventarioFacade.findByMedicamento(med);
        if (optInv.isPresent()) {
            Inventario invt = optInv.get();
            cantidad = invt.getCantidad();
        }
        return cantidad;
    }

    public List<Usuario> getUsuarios() {
        return usuarioFacade.findAll();
    }

    public void saveUser(UsuarioFx usuario) {
        Usuario user = usuarioFxToUsuario(usuario);
        usuarioFacade.create(user);
    }

    public void edit(UsuarioFx usuario) {
        Usuario user = usuarioFxToUsuario(usuario);
        usuarioFacade.edit(user);
    }

    public void deleteUser(UsuarioFx usuario) {
        Usuario user = usuarioFxToUsuario(usuario);
        usuarioFacade.remove(user);
    }

    private Usuario usuarioFxToUsuario(UsuarioFx usuario) {
        Usuario user;
        if (usuario.getId() != 0) {
            user = usuarioFacade.find(usuario.getId());
            if (!usuario.getClave().equals(user.getPassword())) {
                user.setPassword(SecurityUtil.getSecurePassword(usuario.getClave()));
            }
        } else {
            user = new Usuario();
            user.setPassword(SecurityUtil.getSecurePassword(usuario.getClave()));
        }
        user.setNombre(usuario.getNombre());
        user.setApellido(usuario.getApellido());

        user.setUsername(usuario.getUsuario());
        switch (usuario.getPerfil()) {
            case UsuarioFx.PERFIL_ADMIN:
                user.setNivel(Usuario.ADMIN);
                break;
            case UsuarioFx.PERFIL_USER:
                user.setNivel(Usuario.USER);
                break;
            default:
                user.setNivel(Usuario.CONSULTA);
                break;
        }
        return user;
    }

    private UsuarioFx usuarioToUsuarioFx(Usuario usuario) {
        UsuarioFx user = new UsuarioFx();
        user.setId(usuario.getId());
        user.setNombre(usuario.getNombre());
        user.setApellido(usuario.getApellido());
        user.setUsuario(usuario.getUsername());
        switch (usuario.getNivel()) {
            case Usuario.ADMIN:
                user.setPerfil(UsuarioFx.PERFIL_ADMIN);
                break;
            case Usuario.USER:
                user.setPerfil(UsuarioFx.PERFIL_USER);
                break;
            case Usuario.CONSULTA:
                user.setPerfil(UsuarioFx.PERFIL_CONSULTA);
                break;
        }
        return user;
    }

    private MedicamentoFx medicamentoToMedicamentoFx(Medicamento med, MedicamentoFx medFx) {
        medFx.setId(med.getId());
        medFx.setNombre(med.getNombre());
        medFx.setUbicacion(med.getUbicacion());

        return medFx;
    }

    public boolean saveExcelData(File file) {
        boolean ok = false;
        LOG.log(Level.INFO, "comienza a migrar datos");
        try {
            FileInputStream excelFile = new FileInputStream(file);
            Workbook wb = new XSSFWorkbook(excelFile);
            Sheet sheet = wb.getSheetAt(0);
            // Se leen las filas y sus celdas
            boolean eof = false;
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                Object[] medicamentoFeatures = new Object[5];
                for (Cell cell : row) {
                    int cellColumn = cell.getColumnIndex();
                    if (!eof) {
                        switch (cellColumn) {
                            case 0: // nombre
                                if (!cell.getStringCellValue().equals("YYYYY")) {
                                    medicamentoFeatures[0] = cell.getStringCellValue();
                                } else {
                                    eof = true;
                                }
                                break;
                            case 1: // componentes
                                String[] componentes = cell.getStringCellValue().split(":");
                                medicamentoFeatures[1] = componentes;
                                break;
                            case 2: // presentación
                                medicamentoFeatures[2] = cell.getStringCellValue();
                                break;
                            case 5: // usos
                                String[] usos;
                                //if (cell.getCellTypeEnum() == CellType.BLANK) {
                                if (cell.getStringCellValue().equals("XXX")) {
                                    usos = new String[]{""};
                                } else {
                                    usos = cell.getStringCellValue().split("/");
                                }
                                medicamentoFeatures[3] = usos;
                                break;
                            case 4:
                                Double valor;
                                valor = cell.getNumericCellValue();
                                medicamentoFeatures[4] = valor.intValue();
                                break;
                            default:
                                continue;
                        }
                    } else {
                        break;
                    }
                }
                if (!eof) {
                    makeMedicamento(medicamentoFeatures);
                    LOG.log(Level.INFO, "Finaliza migración de datos");
                }
            }
            ok = true;
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error leyendo el archivo: {0}", new Object[]{e});
        }
        return ok;
    }

    private void makeMedicamento(Object[] medicamentoFeatures) {

        // se crea el medicamento
        Medicamento medicamento = new Medicamento();
        medicamento.setNombre((String) medicamentoFeatures[0]);

        // se crean los componentes del medicamento
        String[] componentesString = (String[]) medicamentoFeatures[1];
        List<Componente> componentes = new ArrayList<>();
        for (String componenteName : componentesString) {
            Componente comp = new Componente();
            comp.setNombre(componenteName);
            componentes.add(comp);
        }

        // se crea la presentación del medicamento
        Presentacion presentacion = new Presentacion();
        presentacion.setNombre((String) medicamentoFeatures[2]);

        // se crean los usos
        String[] usosString = (String[]) medicamentoFeatures[3];
        List<Uso> usos = new ArrayList<>();
        for (String usoName : usosString) {
            Uso uso = new Uso();
            uso.setNombreUso(usoName);
            usos.add(uso);
        }

        // se obtiene la cantidad del medicamento
        Integer cant = (Integer) medicamentoFeatures[4];

        // guarda en DB:
        // se cran los medicamentos
        medicamento = medicamentoFacade.create(medicamento);

        // se crean loc componentes
        for (Componente compo : componentes) {
            Optional<Componente> optCompo = componenteFacade.findByNombre(compo.getNombre());
            MedicamentoHasComponente mhc = new MedicamentoHasComponente();
            if (!optCompo.isPresent()) {
                compo = componenteFacade.create(compo);
            } else {
                compo = optCompo.get();
            }
            mhc.setComponenteId(compo);
            mhc.setMedicamentoId(medicamento);
            medHCompFacade.create(mhc);
        }

        // se crean las presentaciones
        Optional<Presentacion> optPresentacion = presentacionFacade.findByNombre(presentacion.getNombre());
        MedicamentoHasPresentacion mhp = new MedicamentoHasPresentacion();
        if (!optPresentacion.isPresent()) {
            presentacion = presentacionFacade.create(presentacion);
        } else {
            presentacion = optPresentacion.get();
        }
        mhp.setMedicamentoId(medicamento);
        mhp.setPresentacionId(presentacion);
        medHPreFacade.create(mhp);

        // se crean los usos
        for (Uso use : usos) {
            Optional<Uso> optUso = usoFacade.findByNombre(use.getNombreUso());
            MedicamentoHasUso mhu = new MedicamentoHasUso();
            if (!optUso.isPresent()) {
                use = usoFacade.create(use);
            } else {
                use = optUso.get();
            }
            mhu.setMedicamentoId(medicamento);
            mhu.setUsoId(use);
            medHUsoFacade.create(mhu);
        }

        // se crea el inventario
        Inventario inventario;
        Optional<Inventario> optInventario = inventarioFacade.findByMedicamento(medicamento);

        int cantidad = cant;
        if (!optInventario.isPresent()) {
            inventario = new Inventario();
            inventario.setMedicamentoId(medicamento);
        } else {
            inventario = optInventario.get();
            cantidad += inventario.getCantidad();
        }
        inventario.setCantidad(cantidad);
        inventarioFacade.edit(inventario);

        // se crea el movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setTipo(Movimiento.ENTRADA);
        movimiento.setCantidad(cant);
        movimiento.setFecha(new Date());
        movimiento.setMedicamentoId(medicamento);
        movimientoFacade.create(movimiento);
    }

    public ObservableList<MedicamentoFx> buscarByNombre(String nombreMed) {
        List<Medicamento> medicamentos = medicamentoFacade.findAllByNombre(nombreMed);
        return parseListToObservableList(medicamentos);
    }

    private ObservableList<MedicamentoFx> parseListToObservableList(List<Medicamento> medicamentos) {
        ObservableList<MedicamentoFx> medicamentosList = FXCollections.observableArrayList();

        medicamentos.forEach((Medicamento med) -> {
            Optional<List<MedicamentoHasPresentacion>> optMedHasPres = medHPreFacade
                    .findAllByMedicamento(med);
            String presentacion;
            if (optMedHasPres.isPresent()) {
                List<MedicamentoHasPresentacion> lista = optMedHasPres.get();
                for (MedicamentoHasPresentacion mhp : lista) {
                    presentacion = mhp.getPresentacionId().getNombre();
                    Optional<List<MedicamentoHasComponente>> optMhcList
                            = medHCompFacade.findAllByMedicamento(med);
                    Optional<List<MedicamentoHasUso>> optMhuList
                            = medHUsoFacade.findAllByMedicamento(med);

                    List<String> componentes = new ArrayList<>();
                    if (optMhcList.isPresent()) {
                        List<MedicamentoHasComponente> mhcList = optMhcList.get();
                        mhcList.forEach((mch) -> {
                            componentes.add(mch.getComponenteId().getNombre());
                        });
                    }

                    List<String> usos = new ArrayList<>();
                    if (optMhuList.isPresent()) {
                        List<MedicamentoHasUso> mhuList = optMhuList.get();
                        mhuList.forEach((mhu) -> {
                            usos.add(mhu.getUsoId().getNombreUso());
                        });
                    }

                    MedicamentoFx medfx = new MedicamentoFx(med.getId(), med.getNombre(),
                            med.getUbicacion(), presentacion, componentes, usos);

                    int cantidad = findCantidadByMedicamento(med);
                    medfx.setCantidad(cantidad);

                    medicamentosList.add(medfx);
                }
            }

        });
        return medicamentosList;
    }
}
