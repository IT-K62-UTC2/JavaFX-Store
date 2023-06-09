package utc2.itk62.store.repositories;

import com.mysql.cj.jdbc.CallableStatement;
import utc2.itk62.store.connection.ConnectionUtil;
import utc2.itk62.store.models.InvoiceDetail;
import utc2.itk62.store.models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailRepo {
    public InvoiceDetailRepo() {
    }

    public List<InvoiceDetail> getAllInvoicesDetailByInvoiceId(int invoiceId) {
        List<InvoiceDetail> invoiceList = new ArrayList<InvoiceDetail>();
            String query = "SELECT invoice_detail.*, product.price, product.name, product.id  FROM invoice_detail " +
                "LEFT JOIN product ON product.id = invoice_detail.id_product WHERE id_invoice = ?";
        try {
            PreparedStatement ptmt = ConnectionUtil.getConnection().prepareStatement(query);
            ptmt.setInt(1, invoiceId);
            ResultSet rs = ptmt.executeQuery();
            while (rs.next()) {
                InvoiceDetail invoiceDetail = new InvoiceDetail();
                invoiceDetail.setId(rs.getInt("id"));
                Product product = new Product();

                product.setName(rs.getString("product.name"));
                product.setId(rs.getInt("product.id"));
                product.setPrice(rs.getDouble("product.price"));
                invoiceDetail.setProduct(product);
                invoiceDetail.setProductQuantity(rs.getInt("product_quantity"));
                invoiceDetail.setMoneyTotal(rs.getDouble("money_total"));
                invoiceDetail.setStatus(rs.getInt("status"));
                invoiceDetail.setCreatedAt(rs.getTimestamp("created_at"));
                invoiceDetail.setUpdatedAt(rs.getTimestamp("updated_at"));
                invoiceList.add(invoiceDetail);
            }
            return invoiceList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionUtil.closeConnection();
        }
    }

    public int createInvoiceDetail(InvoiceDetail invoiceDetail) {
        String query = "INSERT INTO invoice_detail(id_invoice, id_product, product_quantity, money_total)" +
                "VALUES(?, ?, ?, ?)";
        int result = 0;
        Connection conn = ConnectionUtil.getConnection();

        try{
            conn.setAutoCommit(false);
            PreparedStatement ptmt = conn.prepareStatement(query);
            ptmt.setInt(1, invoiceDetail.getInvoice().getId());
            ptmt.setInt(2, invoiceDetail.getProduct().getId());
            ptmt.setInt(3, invoiceDetail.getProductQuantity());
            ptmt.setDouble(4, invoiceDetail.getMoneyTotal());
            result = ptmt.executeUpdate();
            conn.commit();
        }catch (SQLException e){
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        } finally {
            ConnectionUtil.closeConnection();
        }
        return result;
    }

    public boolean insertInvoiceDetail(InvoiceDetail invoiceDetail) {
        Connection conn = null;
        // Thiết lập giá trị cho các tham số của Stored Procedure
        try {
            conn = ConnectionUtil.getConnection();
            conn.setAutoCommit(false); // Bật chế độ transaction
            CallableStatement callableStatement = (CallableStatement) ConnectionUtil.getConnection().prepareCall("{CALL insert_invoice_detail(?, ?, ?, ?)}");
            callableStatement.setInt(1, invoiceDetail.getInvoice().getId());
            callableStatement.setInt(2, invoiceDetail.getProduct().getId());
            callableStatement.setInt(3, invoiceDetail.getProductQuantity());
            callableStatement.setDouble(4, invoiceDetail.getMoneyTotal());
            // Thực thi Stored Procedure
            boolean result = callableStatement.execute();
            conn.commit();
            return result;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
}
