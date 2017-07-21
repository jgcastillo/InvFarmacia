package org.farmacia.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.farmacia.jpacontroller.ComponenteFacade;
import org.farmacia.jpacontroller.EquivalenteFacade;
import org.farmacia.jpacontroller.MedicamentoFacade;
import org.farmacia.jpacontroller.PresentacionFacade;
import org.farmacia.jpacontroller.UsoFacade;
import org.farmacia.jpamodel.Componente;
import org.farmacia.jpamodel.Equivalente;
import org.farmacia.jpamodel.Medicamento;
import org.farmacia.jpamodel.Presentacion;
import org.farmacia.jpamodel.Uso;

/**
 * Esta clase permite suministrar todos los datos iniciales de los TextFields con
 * autobusqueda
 * @author jgcastillo
 */
public class MainViewData {
    
    private final MedicamentoFacade medicamentoFacade;
    private final ComponenteFacade componenteFacade;
    private final UsoFacade usoFacade;
    private final PresentacionFacade presentacionFacade;
    private final EquivalenteFacade equivalenteFacade;

    public MainViewData() {
        this.medicamentoFacade = new MedicamentoFacade();
        this.componenteFacade = new ComponenteFacade();
        this.usoFacade = new UsoFacade();
        this.presentacionFacade = new PresentacionFacade();
        this.equivalenteFacade = new EquivalenteFacade();
    }
    
    public String[] loadInitialMedicaments(){
        List<Medicamento> medicamentos = medicamentoFacade.findAll();
        List<String> medicamentosNames = medicamentos.stream()
                                    .map(med -> med.getNombre())
                                    .collect(Collectors.toList());
        Set<String> mediSet = new HashSet<>(medicamentosNames);
        String[] mediNombre = new String[mediSet.size()];
        int i = 0;
        for (String med : mediSet) {
            mediNombre[i] = med;
            i++;
        }

        return mediNombre;
    }
    
    public String[] loadInitialComponentes() {
        List<Componente> componentes = componenteFacade.findAll();
        Set<String> nombres = new HashSet<>();
        for(Componente componente : componentes){
            String[] partes = componente.getNombre().split(" ");
            for(String parte : partes){
                nombres.add(parte);
            }
        }

        String[] componentNombre = new String[nombres.size()];
        int i = 0;
        for (String comp : nombres) {
            componentNombre[i] = comp;
            i++;
        }

        return componentNombre;
    }
    
    public String[] loadInitialUsos() {
        List<Uso> usos = usoFacade.findAll();
        Set<Uso> usoSet = new HashSet<>(usos);
        String[] usoNombre = new String[usoSet.size()];
        int i = 0;
        for (Uso uso : usoSet) {
            usoNombre[i] = uso.getNombreUso();
            i++;
        }

        return usoNombre;
    }
    
    public String[] loadInitialPresentaciones() {
        List<Presentacion> presentaciones = presentacionFacade.findAll();
        Set<Presentacion> presentacionSet = new HashSet<>(presentaciones);
        String[] presentacionNombre = new String[presentacionSet.size()];
        int i = 0;
        for (Presentacion presentacion : presentacionSet) {
            presentacionNombre[i] = presentacion.getNombre();
            i++;
        }

        return presentacionNombre;
    }
    
    public String[] loadInitialEquivalentes() {
        List<Equivalente> equivalentes = equivalenteFacade.findAll();
        Set<Equivalente> equivalenteSet = new HashSet<>(equivalentes);
        String[] equivNombre = new String[equivalenteSet.size()];
        int i = 0;
        for (Equivalente equi : equivalenteSet) {
            equivNombre[i] = equi.getNombre();
            i++;
        }

        return equivNombre;
    }
}
