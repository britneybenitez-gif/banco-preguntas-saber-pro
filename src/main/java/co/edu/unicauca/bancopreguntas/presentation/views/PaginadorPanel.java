package co.edu.unicauca.bancopreguntas.presentation.views;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import co.edu.unicauca.bancopreguntas.domain.entities.Pagina;

/** Barra de paginación reutilizable: anterior / siguiente, resumen y tamaño de página. */
public class PaginadorPanel extends JPanel {

    private final JButton btnAnterior = new JButton("◀ Anterior");
    private final JButton btnSiguiente = new JButton("Siguiente ▶");
    private final JLabel lblResumen = new JLabel("Sin resultados");
    private final JComboBox<Integer> cmbTamanio = new JComboBox<>(new Integer[]{5, 10, 20, 50});
    private final Runnable alCambiar;
    private int pagina = 1;

    /** @param alCambiar se ejecuta cuando cambia la página o el tamaño de página */
    public PaginadorPanel(Runnable alCambiar) {
        super(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        this.alCambiar = alCambiar;
        cmbTamanio.setSelectedItem(10);

        add(new JLabel("Filas por página:"));
        add(cmbTamanio);
        add(btnAnterior);
        add(lblResumen);
        add(btnSiguiente);

        btnAnterior.setEnabled(false);
        btnSiguiente.setEnabled(false);

        btnAnterior.addActionListener(e -> {
            pagina--;
            alCambiar.run();
        });
        btnSiguiente.addActionListener(e -> {
            pagina++;
            alCambiar.run();
        });
        cmbTamanio.addActionListener(e -> {
            pagina = 1;
            alCambiar.run();
        });
    }

    public int getPagina() {
        return pagina;
    }

    public int getTamanio() {
        return (Integer) cmbTamanio.getSelectedItem();
    }

    /** Vuelve a la primera página (se usa al aplicar un filtro nuevo). */
    public void reiniciar() {
        pagina = 1;
    }

    /** Sincroniza la barra con el resultado de la última consulta. */
    public void actualizar(Pagina<?> resultado) {
        pagina = resultado.pagina();
        lblResumen.setText(resultado.totalElementos() == 0
                ? "Sin resultados"
                : "Página " + resultado.pagina() + " de " + resultado.totalPaginas()
                  + "  (" + resultado.totalElementos() + " preguntas)");
        btnAnterior.setEnabled(resultado.hayAnterior());
        btnSiguiente.setEnabled(resultado.haySiguiente());
    }
}
