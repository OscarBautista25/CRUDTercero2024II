import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class GestionRoles extends JFrame{
    private JTextField idText;
    private JTextField rolText;
    private JTextField nombreText;
    private JButton consultarBoton;
    private JButton agregarBoton;
    private JButton actualizarBoton;
    private JButton eliminarBoton;
    private JList lista;
    private JTable tabla;
    private JPanel panelRoles;
    Connection conexion;
    DefaultListModel modLista = new DefaultListModel<>();
    PreparedStatement ps;
    String[] campos = {"id","rol","nombre"};
    String[] registros = new String[10];
    DefaultTableModel modTabla = new DefaultTableModel(null,campos);
    Statement st;
    ResultSet rs;

    public GestionRoles(){

        consultarBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    consultar();
                }catch (SQLException ex){
                    throw new RuntimeException(ex);
                }

            }
        });
        agregarBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    insertar();
                }catch (SQLException ex){
                    throw new RuntimeException(ex);
                }
            }
        });
        actualizarBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    actualizar();
                }catch (SQLException ex){
                    throw new RuntimeException(ex);
                }
            }
        });
        eliminarBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    eliminar();
                }catch (SQLException ex){
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    void conectar(){
        try{
            conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/pitercero","root","1234");
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    void consultar() throws SQLException{
        conectar();
        modTabla.setRowCount(0);
        tabla.setModel(modTabla);
        st = conexion.createStatement();
        rs= st.executeQuery("select id, rol, nombre from roles");
        while (rs.next()){
            registros[0]= rs.getString("id");
            registros[1]= rs.getString("rol");
            registros[2]= rs.getString("nombre");
            modTabla.addRow(registros);
        }
    }
    void insertar() throws SQLException{
        conectar();
        ps = conexion.prepareStatement("insert into roles(id, rol, nombre) values (?,?,?)");
        ps.setInt(1,Integer.parseInt(idText.getText()));
        ps.setString(2,rolText.getText());
        ps.setString(3,nombreText.getText());
        if(ps.executeUpdate()>0) {
            lista.setModel(modLista);
            modLista.removeAllElements();
            modLista.addElement("Elemento Agregado");

            idText.setText("");
            rolText.setText("");
            nombreText.setText("");
            consultar();
        }

    }
    void actualizar() throws SQLException{
        conectar();
        ps = conexion.prepareStatement("update roles set rol=?, nombre=? where id=?");
        ps.setString(1,rolText.getText());
        ps.setString(2,nombreText.getText());
        ps.setInt(3,Integer.parseInt(idText.getText()));
        if(ps.executeUpdate()>0) {
            lista.setModel(modLista);
            modLista.removeAllElements();
            modLista.addElement("Elemento Actualizado");

            idText.setText("");
            rolText.setText("");
            nombreText.setText("");
            consultar();
        }

    }
    void eliminar() throws SQLException{
        conectar();
        ps = conexion.prepareStatement("delete from roles where id=?");
        ps.setInt(1,Integer.parseInt(idText.getText()));
        if(ps.executeUpdate()>0) {
            lista.setModel(modLista);
            modLista.removeAllElements();
            modLista.addElement("Elemento Eliminado");

            idText.setText("");
            rolText.setText("");
            nombreText.setText("");
            consultar();
        }

    }

    public static void main(String[] args) {
        GestionRoles gr = new GestionRoles();
        gr.setContentPane(new GestionRoles().panelRoles);
        gr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gr.setVisible(true);
        gr.pack();
    }


}
