package org.farmacia.initialdata;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.farmacia.jpacontroller.ComponenteFacade;
import org.farmacia.jpacontroller.MedicamentoFacade;
import org.farmacia.jpacontroller.MedicamentoHasComponenteFacade;
import org.farmacia.jpacontroller.MedicamentoHasPresentacionFacade;
import org.farmacia.jpacontroller.MedicamentoHasUsoFacade;
import org.farmacia.jpacontroller.PresentacionFacade;
import org.farmacia.jpacontroller.UsoFacade;
import org.farmacia.jpacontroller.UsuarioFacade;
import org.farmacia.jpamodel.Componente;
import org.farmacia.jpamodel.Medicamento;
import org.farmacia.jpamodel.MedicamentoHasComponente;
import org.farmacia.jpamodel.MedicamentoHasPresentacion;
import org.farmacia.jpamodel.MedicamentoHasUso;
import org.farmacia.jpamodel.Presentacion;
import org.farmacia.jpamodel.Uso;
import org.farmacia.jpamodel.Usuario;
import org.farmacia.utils.SecurityUtil;

/**
 * Será eliminada al momento de crear toda la estructura
 *
 * @author jgcastillo
 */
public class InitialData {

    private final MedicamentoFacade medicamentoFacade = new MedicamentoFacade();
    private final ComponenteFacade componenteFacade = new ComponenteFacade();
    private final PresentacionFacade presentacionFacade = new PresentacionFacade();
    private final UsoFacade usoFacade = new UsoFacade();
    private final UsuarioFacade usuarioFacade = new UsuarioFacade();

    private final MedicamentoHasComponenteFacade mhcFacade = new MedicamentoHasComponenteFacade();
    private final MedicamentoHasPresentacionFacade mhpFacade = new MedicamentoHasPresentacionFacade();
    private final MedicamentoHasUsoFacade mhuFacade = new MedicamentoHasUsoFacade();

    public void loadInitialData() {
        
        // se crea el usuario inicial
        Usuario user = new Usuario();
        user.setUsername("goyo");
        user.setPassword(SecurityUtil.getSecurePassword("spontecorp"));
        user.setNivel(Usuario.SUPER_ADMIN);
        
        usuarioFacade.create(user);
        

        // usos
//        Uso[] usoArray = {
//            new Uso("Antipirético"),
//            new Uso("Analgésico"),
//            new Uso("Diabetes"),
//            new Uso("Anticoagulante"),
//            new Uso("Malestar General"),
//            new Uso("Antigripal")
//        };
//        List<Uso> usos = Arrays.asList(usoArray);
//        usos.forEach(u -> usoFacade.create(u));
//
//        // presentaciones
//        Presentacion[] presentacionArray = {
//            new Presentacion("Pastillas"),
//            new Presentacion("Solución Oral"),
//            new Presentacion("Jarabe")    
//        };
//        List<Presentacion> presentaciones = Arrays.asList(presentacionArray);
//        presentaciones.forEach(pre -> presentacionFacade.create(pre));
//
//        // componentes
//        Componente[] componenteArray = {
//            new Componente("Tramadol"),
//            new Componente("Paracetamol"),
//            new Componente("Arcabosa"),
//            new Componente("Aceclofenaco"),
//            new Componente("Acenocumarol"),
//            new Componente("Acetaminofen")
//        };
//
//        List<Componente> componentes = Arrays.asList(componenteArray);
//        componentes.forEach(comp -> componenteFacade.create(comp));
//
//        // medicamentos
//        Medicamento[] medicamentoArray = {
//            new Medicamento("Dolnot Forte"),
//            new Medicamento("Glucobay"),
//            new Medicamento("Tevagen"),
//            new Medicamento("Sintrom"),
//            new Medicamento("Paracetamol"),
//            new Medicamento("Excedrin"),};
//
//        List<Medicamento> medicamentos = Arrays.asList(medicamentoArray);
//        medicamentos.forEach(med -> medicamentoFacade.create(med));
//
//        // se crea la relaciones
//        List<Medicamento> medList = medicamentoFacade.findAllByNombre("Dolnot Forte");
//        Optional<Componente> optComp1 = componenteFacade.findByNombre("Tramadol");
//        Optional<Componente> optComp2 = componenteFacade.findByNombre("Paracetamol");
//        Optional<Presentacion> optPres = presentacionFacade.findByNombre("Pastillas");
//        Optional<Uso> optUso1 = usoFacade.findByNombre("Antipirético");
//        Optional<Uso> optUso2 = usoFacade.findByNombre("Analgésico");
//        assignComponenteToMedicamento(medList.get(0), optComp1.get(), optComp2.get());
//        assignPresentacionToMedicamento(medList.get(0), optPres.get());
//        assignUsoToMedicamento(medList.get(0), optUso1.get(), optUso2.get());
//        
//        medList = medicamentoFacade.findAllByNombre("Glucobay");
//        optComp1 = componenteFacade.findByNombre("Arcabosa");
//        optPres = presentacionFacade.findByNombre("Pastillas");
//        optUso1 = usoFacade.findByNombre("Diabetes");
//        assignComponenteToMedicamento(medList.get(0), optComp1.get());
//        assignPresentacionToMedicamento(medList.get(0), optPres.get());
//        assignUsoToMedicamento(medList.get(0), optUso1.get());
//        
//        medList = medicamentoFacade.findAllByNombre("Tevagen");
//        optComp1 = componenteFacade.findByNombre("Aceclofenaco");
//        optPres = presentacionFacade.findByNombre("Pastillas");
//        optUso1 = usoFacade.findByNombre("Analgésico");
//        assignComponenteToMedicamento(medList.get(0), optComp1.get());
//        assignPresentacionToMedicamento(medList.get(0), optPres.get());
//        assignUsoToMedicamento(medList.get(0), optUso1.get());
//        
//        medList = medicamentoFacade.findAllByNombre("Tevagen");
//        optComp1 = componenteFacade.findByNombre("Aceclofenaco");
//        optPres = presentacionFacade.findByNombre("Jarabe");
//        optUso1 = usoFacade.findByNombre("Analgésico");
//        assignComponenteToMedicamento(medList.get(0), optComp1.get());
//        assignPresentacionToMedicamento(medList.get(0), optPres.get());
//        assignUsoToMedicamento(medList.get(0), optUso1.get());
//        
//        medList = medicamentoFacade.findAllByNombre("Sintrom");
//        optComp1 = componenteFacade.findByNombre("Acenocumarol");
//        optPres = presentacionFacade.findByNombre("Pastillas");
//        optUso1 = usoFacade.findByNombre("Anticoagulante");
//        assignComponenteToMedicamento(medList.get(0), optComp1.get());
//        assignPresentacionToMedicamento(medList.get(0), optPres.get());
//        assignUsoToMedicamento(medList.get(0), optUso1.get());
//        
//        medList = medicamentoFacade.findAllByNombre("Paracetamol");
//        optComp1 = componenteFacade.findByNombre("Acetaminofen");
//        optPres = presentacionFacade.findByNombre("Pastillas");
//        optUso1 = usoFacade.findByNombre("Malestar General");
//        assignComponenteToMedicamento(medList.get(0), optComp1.get());
//        assignPresentacionToMedicamento(medList.get(0), optPres.get());
//        assignUsoToMedicamento(medList.get(0), optUso1.get());
//        
//        medList = medicamentoFacade.findAllByNombre("Excedrin");
//        optComp1 = componenteFacade.findByNombre("Acetaminofen");
//        optPres = presentacionFacade.findByNombre("Solución Oral");
//        optUso1 = usoFacade.findByNombre("Antigripal");
//        assignComponenteToMedicamento(medList.get(0), optComp1.get());
//        assignPresentacionToMedicamento(medList.get(0), optPres.get());
//        assignUsoToMedicamento(medList.get(0), optUso1.get());
    }
    
    private void assignComponenteToMedicamento(Medicamento med, Componente... componentes) {
        MedicamentoHasComponente mhc;
        for (Componente comp : componentes) {
            mhc = new MedicamentoHasComponente();

            mhc.setMedicamentoId(med);
            mhc.setComponenteId(comp);
            mhcFacade.create(mhc);
        }
    }

    private void assignPresentacionToMedicamento(Medicamento med, Presentacion... presentaciones) {
        MedicamentoHasPresentacion mhp;
        for (Presentacion pres : presentaciones) {
            mhp = new MedicamentoHasPresentacion();

            mhp.setMedicamentoId(med);
            mhp.setPresentacionId(pres);
            mhpFacade.create(mhp);
        }
    }
    
    private void assignUsoToMedicamento(Medicamento med, Uso... usos) {
        MedicamentoHasUso mhu;
        for (Uso uso : usos) {
            mhu = new MedicamentoHasUso();

            mhu.setMedicamentoId(med);
            mhu.setUsoId(uso);
            mhuFacade.create(mhu);
        }
    }
}
