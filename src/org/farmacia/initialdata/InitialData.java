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
