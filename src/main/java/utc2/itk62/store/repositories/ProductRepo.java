package utc2.itk62.store.repositories;

import utc2.itk62.store.connection.ConnectionUtil;
import utc2.itk62.store.models.Category;
import utc2.itk62.store.models.Product;
import utc2.itk62.store.models.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepo {
    public ProductRepo() {
    }

    public int updateQuantityProduct(Product product) {
        String query = "UPDATE product SET quantity = quantity + ? WHERE id = ?";
        int result = 0;
        Connection conn = ConnectionUtil.getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement ptmt = conn.prepareStatement(query);
            ptmt.setInt(1, product.getQuantity());
            ptmt.setInt(2, product.getId());
            result = ptmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
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

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<Product>();
        String query = "SELECT product.*, category.name, category.id, supplier.name, supplier.id FROM product " +
                " LEFT JOIN supplier ON supplier.id = product.id_supplier" +
                " LEFT JOIN category ON category.id = product.id_category" +
                " WHERE product.status = 1 ORDER BY product.created_at DESC FOR UPDATE";
        try {
            PreparedStatement ptmt = ConnectionUtil.getConnection().prepareStatement(query);
            ResultSet rs = ptmt.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                Supplier supplier = new Supplier();
                category.setId(rs.getInt("category.id"));
                category.setName(rs.getString("category.name"));
                supplier.setName(rs.getString("supplier.name"));
                supplier.setId(rs.getInt("supplier.id"));
                Product product = new Product();
                product.setCategory(category);
                product.setImportPrice(rs.getDouble("product.import_price"));
                product.setSupplier(supplier);
                product.setQuantity(rs.getInt("product.quantity"));
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("product.name"));
                product.setDescription(rs.getString("desc"));
                product.setPrice(rs.getDouble("price"));
                product.setAvatar(rs.getString("avatar"));
                product.setStatus(rs.getInt("status"));
                product.setCreatedAt(rs.getTimestamp("created_at"));
                product.setUpdatedAt(rs.getTimestamp("updated_at"));
                productList.add(product);
            }
            return productList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionUtil.closeConnection();
        }
    }

    public List<Product> getProductsByNameAndCategory(String name, int idCategory) {
        List<Product> productList = new ArrayList<Product>();
//        String query = "SELECT product.*, category.name, category.id, supplier.name, supplier.id FROM product " +
//                " LEFT JOIN supplier ON supplier.id = product.id_supplier" +
//                " LEFT JOIN category ON category.id = product.id_category" +
//                " WHERE product.status = 1 AND product.name LIKE '%keyword%' ORDER BY product.created_at DESC";
        String query = "SELECT * FROM product " +
//                " LEFT JOIN supplier ON supplier.id = product.id_supplier" +
//                " LEFT JOIN category ON category.id = product.id_category" +
                " WHERE `status` = 1 AND `name` LIKE CONCAT('%', ?, '%') AND id_category = ? ORDER BY created_at DESC FOR UPDATE";
        try {
            PreparedStatement ptmt = ConnectionUtil.getConnection().prepareStatement(query);
            ptmt.setString(1, name);
            ptmt.setInt(2, idCategory);
            ResultSet rs = ptmt.executeQuery();
            while (rs.next()) {
                System.out.println("FUND");
//                Category category = new Category();
//                Supplier supplier = new Supplier();
//                category.setId(rs.getInt("category.id"));
//                category.setName(rs.getString("category.name"));
//                supplier.setName(rs.getString("supplier.name"));
//                supplier.setId(rs.getInt("supplier.id"));
                Product product = new Product();
//                product.setCategory(category);
                product.setImportPrice(rs.getDouble("product.import_price"));
//                product.setSupplier(supplier);
                product.setQuantity(rs.getInt("product.quantity"));
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("product.name"));
                product.setDescription(rs.getString("desc"));
                product.setPrice(rs.getDouble("price"));
                product.setAvatar(rs.getString("avatar"));
                product.setStatus(rs.getInt("status"));
                product.setCreatedAt(rs.getTimestamp("created_at"));
                product.setUpdatedAt(rs.getTimestamp("updated_at"));
                productList.add(product);
            }
            return productList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionUtil.closeConnection();
        }
    }

    public Product getProductByIdAndIdCategory(int id, int idCategory) {
        Product product = null;
//        String query = "SELECT product.*, category.name, category.id, supplier.name, supplier.id FROM product " +
//                " LEFT JOIN supplier ON supplier.id = product.id_supplier" +
//                " LEFT JOIN category ON category.id = product.id_category" +
//                " WHERE product.status = 1 AND product.name LIKE '%keyword%' ORDER BY product.created_at DESC";
        String query = "SELECT * FROM product " +
//                " LEFT JOIN supplier ON supplier.id = product.id_supplier" +
//                " LEFT JOIN category ON category.id = product.id_category" +
                " WHERE `status` = 1 AND id = ? AND id_category = ? ORDER BY created_at DESC";
        try {
            PreparedStatement ptmt = ConnectionUtil.getConnection().prepareStatement(query);
            ptmt.setInt(1, id);
            ptmt.setInt(1, idCategory);
            ResultSet rs = ptmt.executeQuery();
            while (rs.next()) {
//                Category category = new Category();
//                Supplier supplier = new Supplier();
//                category.setId(rs.getInt("category.id"));
//                category.setName(rs.getString("category.name"));
//                supplier.setName(rs.getString("supplier.name"));
//                supplier.setId(rs.getInt("supplier.id"));
                product = new Product();
//                product.setCategory(category);
                product.setImportPrice(rs.getDouble("product.import_price"));
//                product.setSupplier(supplier);
                product.setQuantity(rs.getInt("product.quantity"));
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("product.name"));
                product.setDescription(rs.getString("desc"));
                product.setPrice(rs.getDouble("price"));
                product.setAvatar(rs.getString("avatar"));
                product.setStatus(rs.getInt("status"));
                product.setCreatedAt(rs.getTimestamp("created_at"));
                product.setUpdatedAt(rs.getTimestamp("updated_at"));
            }
            return product;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionUtil.closeConnection();
        }
    }

    public List<Product> getProductListByIdCategory(int idCategory) {
        List<Product> productList = new ArrayList<Product>();
        String query = "SELECT product.*, supplier.name FROM product" +
                " LEFT JOIN supplier ON supplier.id = product.id_supplier" +
                " WHERE product.status = 1 AND product.id_category = ? FOR UPDATE";
        try {
            PreparedStatement ptmt = ConnectionUtil.getConnection().prepareStatement(query);
            ptmt.setInt(1, idCategory);
            ResultSet rs = ptmt.executeQuery();
            while (rs.next()) {
                // Product
                Product product = new Product();
                product.setId(rs.getInt("id"));
//                product.setIdSupplier(rs.getInt("id_supplier"));
//                product.setIdCategory(rs.getInt("id_category"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("desc"));
                product.setImportPrice(rs.getDouble("product.import_price"));
                product.setPrice(rs.getDouble("price"));
                product.setAvatar(rs.getString("avatar"));
                product.setStatus(rs.getInt("status"));
                product.setQuantity(rs.getInt("quantity"));
                product.setCreatedAt(rs.getTimestamp("created_at"));
                product.setUpdatedAt(rs.getTimestamp("updated_at"));

                // JOIN table supplier
                Supplier supplier = new Supplier();
                supplier.setName(rs.getString("supplier.name"));
//                supplier.setEmail(rs.getString("supplier.email"));
//                supplier.setId(rs.getInt("supplier.id"));
//                supplier.setPhoneNumber(rs.getString("supplier.phone_number"));
//                supplier.setAddress(rs.getString("supplier.address"));
//                supplier.set
                product.setSupplier(supplier);
                productList.add(product);
            }
            return productList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionUtil.closeConnection();
        }
    }

    public int createProduct(Product product) {
        String query = "INSERT INTO product(id_supplier, id_category, `name`, `desc`, price, avatar, import_price, quantity)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        int result = 0;
        Connection conn = ConnectionUtil.getConnection();

        try {
            conn.setAutoCommit(false);
            PreparedStatement ptmt = conn.prepareStatement(query);
            ptmt.setInt(1, product.getSupplier().getId());
            ptmt.setInt(2, product.getCategory().getId());
            ptmt.setString(3, product.getName());
            ptmt.setString(4, product.getDescription());
            ptmt.setDouble(5, product.getPrice());
            ptmt.setString(6, product.getAvatar());
            ptmt.setDouble(7, product.getImportPrice());
            ptmt.setInt(8, product.getQuantity());
            System.out.println(query);
            result = ptmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionUtil.closeConnection();
        }
        return result;
    }

    public int updateProduct(Product product) {
        String query = "UPDATE product SET " +
                "id_supplier = ?, " +
                "id_category = ?, " +
                "`name` = ?, " +
                "`desc` = ?, " +
                "price = ?, " +
                "avatar = ?, " +
                "quantity = ?, " +
                "import_price = ?" +
                "WHERE id = ?";
        int result = 0;
        Connection conn = ConnectionUtil.getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement ptmt = conn.prepareStatement(query);
            ptmt.setInt(1, product.getSupplier().getId());
            ptmt.setInt(2, product.getCategory().getId());
            ptmt.setString(3, product.getName());
            ptmt.setString(4, product.getDescription());
            ptmt.setDouble(5, product.getPrice());
            ptmt.setString(6, product.getAvatar());
            ptmt.setInt(7, product.getQuantity());
            ptmt.setDouble(8, product.getImportPrice());
            ;
            ptmt.setInt(9, product.getId());
            result = ptmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
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

    public int deleteProduct(int productId) {
        String query = "UPDATE product SET status = 0 WHERE id = ?";
        int result = 0;
        Connection conn = ConnectionUtil.getConnection();

        try {
            conn.setAutoCommit(false);
            PreparedStatement ptmt = conn.prepareStatement(query);
            ptmt.setInt(1, productId);
            result = ptmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
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

    public List<Product> getProductListByIdSupplier(int id) {
        List<Product> productList = new ArrayList<Product>();
        String query = "SELECT product.*, category.name FROM product" +
                " LEFT JOIN category ON category.id = product.id_category" +
                " WHERE product.status = 1 AND product.id_supplier = ?";
        try {
            PreparedStatement ptmt = ConnectionUtil.getConnection().prepareStatement(query);
            ptmt.setInt(1, id);
            ResultSet rs = ptmt.executeQuery();
            while (rs.next()) {
                // Product
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("desc"));
                product.setPrice(rs.getDouble("price"));
                product.setImportPrice(rs.getDouble("import_price"));
                product.setAvatar(rs.getString("avatar"));
                product.setStatus(rs.getInt("status"));
                product.setQuantity(rs.getInt("quantity"));
                product.setCreatedAt(rs.getTimestamp("created_at"));
                product.setUpdatedAt(rs.getTimestamp("updated_at"));

                // JOIN table category
                Category category = new Category();
                ;
                category.setName(rs.getString("category.name"));
//                supplier.setEmail(rs.getString("supplier.email"));
//                supplier.setId(rs.getInt("supplier.id"));
//                supplier.setPhoneNumber(rs.getString("supplier.phone_number"));
//                supplier.setAddress(rs.getString("supplier.address"));
//                supplier.set
                product.setCategory(category);
                productList.add(product);
            }
            return productList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionUtil.closeConnection();
        }
    }
}
