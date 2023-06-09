package utc2.itk62.store.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utc2.itk62.store.models.Category;
import utc2.itk62.store.models.Product;
import utc2.itk62.store.models.Supplier;
import utc2.itk62.store.services.CategoryService;
import utc2.itk62.store.services.ProductService;
import utc2.itk62.store.util.CustomAlert;
import utc2.itk62.store.util.FormatDouble;

import java.sql.Timestamp;

public class CategoryController {
    private static final CategoryService categoryService = new CategoryService();
    private static final ProductService productService = new ProductService();

    @FXML
    public TextField searchValue;
    @FXML
    public TableView<Category> tableListCategory;
    @FXML
    public TableColumn<Category, Integer> colSttCategory;
    @FXML
    public TableColumn<Category, Integer> colIdCategory;
    @FXML
    public TableColumn<Category, String> colNameCategory;
    @FXML
    public TableColumn<Category, Integer> colStatusCategory;
    @FXML
    public TableColumn<Category, Timestamp> colCreatedAtCategory;
    @FXML
    public TableColumn<Category, Timestamp> colUpdatedAtCategory;
    @FXML
    public TableColumn<Product, Integer> colSTTProduct;
    @FXML
    public TableColumn<Product, Supplier> colSupplierProduct;
    @FXML
    public TableColumn<Product, String> colNameProduct;
    @FXML
    public TableColumn<Product, String> colDescProduct;
    @FXML
    public TableColumn<Product, String> colPriceProduct;
    @FXML
    public TableColumn<Product, Integer> colStatusProduct;
    @FXML
    public TableColumn<Product, Integer> colQuantity;
    @FXML
    public TableColumn<Product, Timestamp> colCreatedAtProduct;
    @FXML
    public TableColumn<Product, Timestamp> colUpdatedAtProduct;
    @FXML
    public TableView<Product> tableListProduct;
    @FXML
    public TextField idCategory;
    @FXML
    public TextField nameCategory;
    @FXML
    public ComboBox<String> keySearch;
    @FXML
    public Button btnExportExcel;
    @FXML
    public Button btnDeleteCategory;
    @FXML
    public Button btnAddCategory;
    @FXML
    public Button btnUpdateCategory;


    private ObservableList<Category> categoryList;
    private ObservableList<String> listKeySearch = FXCollections.observableArrayList("ID", "Name");

    public void initialize() {
        // Add keysearch
        keySearch.setItems(listKeySearch);
        keySearch.setValue("ID");
        reloadTableView();
        setupBtnExportExcel();
        setupHandleSearch();
        setUpTableListCategory();
        setUpUpdateCategory();
        setUpAddCategory();
        setUpDeleteCategory();
    }


    // Setup handle search
    private void setupHandleSearch() {
        searchValue.setOnKeyReleased(keyEvent -> {
            // Khởi tạo FilteredList và gán nó với danh sách positionList
            FilteredList<Category> filteredList = new FilteredList<>(categoryList, p -> true);

            // Gán FilteredList làm nguồn dữ liệu cho TableView
            tableListCategory.setItems(filteredList);
            String searchText = searchValue.getText().toLowerCase();
            filteredList.setPredicate(p -> {
                if (searchText.isEmpty()) {
                    return true;
                }
                if ("Name".equals(keySearch.getValue())) {
                    return p.getName().toLowerCase().contains(searchText);
                }
                return String.valueOf(p.getId()).toLowerCase().contains(searchText);
            });
            tableListCategory.getSelectionModel().selectFirst();
            updateCurrentCategoryToForm();
            updateProductCurrentRowCategory();
        });
    }

    private void setUpUpdateCategory() {
        btnUpdateCategory.setOnAction(actionEvent ->{
            if(idCategory.getText().equals("")) {
                return;
            }
            Category category = new Category();
            if(nameCategory.getText().equals("")) {
                CustomAlert.showAlert(Alert.AlertType.ERROR, idCategory.getScene().getWindow(), "Form Error!","Invalid category name");
                nameCategory.requestFocus();
                return;
            }
            category.setId(Integer.parseInt(idCategory.getText()));
            category.setName(nameCategory.getText());
            if(!categoryService.updateCategory(category)) {
                CustomAlert.showAlert(Alert.AlertType.ERROR, idCategory.getScene().getWindow(),"Error!", "Sometimes the category service is not available");
                return;
            }
            CustomAlert.showAlert(Alert.AlertType.INFORMATION, idCategory.getScene().getWindow(),"Success!", "Update category successfully");
            reloadTableView();
        });
    }

    private void setUpDeleteCategory() {
        btnDeleteCategory.setOnAction(actionEvent -> {
            Category category = tableListCategory.getSelectionModel().getSelectedItem();
            if(category == null) {
                return;
            }
            if(!CustomAlert.showAlert(Alert.AlertType.CONFIRMATION, idCategory.getScene().getWindow(),"Delete Category", "Are you sure you want to delete this category")) {
                return;
            }
            if(!categoryService.deleteCategory(category)) {
                CustomAlert.showAlert(Alert.AlertType.ERROR, idCategory.getScene().getWindow(), "Error!","Sometimes the category service is not available");
                return;
            }
            reloadTableView();
        });
    }

    private void setUpAddCategory() {
        btnAddCategory.setOnAction(actionEvent ->{
            Category category = new Category();
            if(!idCategory.getText().equals("")) {
                cleanForm();
            }
            if(nameCategory.getText().equals("")) {
                CustomAlert.showAlert(Alert.AlertType.ERROR, idCategory.getScene().getWindow(), "Form Error!","Invalid category name");
                nameCategory.requestFocus();
                return;
            }
            category.setName(nameCategory.getText());
            if(!categoryService.createCategory(category)) {
                CustomAlert.showAlert(Alert.AlertType.ERROR,idCategory.getScene().getWindow(), "Error","Sometimes the category service is not available");
                return;
            }
            CustomAlert.showAlert(Alert.AlertType.INFORMATION, idCategory.getScene().getWindow(), "Success","Add category successfully");
            reloadTableView();
        });
    }

    private void setUpTableListCategory() {
        tableListCategory.setOnMouseClicked(mouseEvent -> {
            updateProductCurrentRowCategory();
            updateCurrentCategoryToForm();
        });
        tableListCategory.setOnKeyPressed(keyEvent -> {
            updateProductCurrentRowCategory();
            updateCurrentCategoryToForm();
        });
    }


    // setup handle export excel
    private void setupBtnExportExcel() {
        btnExportExcel.setOnAction(action -> {
            categoryService.exportExcel(categoryList, idCategory.getScene().getWindow());
        });
    }


    private void reloadTableView() {
        if (!tableListProduct.getItems().isEmpty()) {
            tableListProduct.getItems().clear();
        }
        if (!tableListCategory.getItems().isEmpty()) {
            tableListCategory.getItems().clear();
        }
        categoryList = FXCollections.observableArrayList(categoryService.getAllCategory());
        colIdCategory.setCellValueFactory(new PropertyValueFactory<Category, Integer>("id"));
        colNameCategory.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));
        colStatusCategory.setCellValueFactory(new PropertyValueFactory<Category, Integer>("status"));
        colCreatedAtCategory.setCellValueFactory(new PropertyValueFactory<Category, Timestamp>("createdAt"));
        colUpdatedAtCategory.setCellValueFactory(new PropertyValueFactory<Category, Timestamp>("updatedAt"));
        tableListCategory.setItems(categoryList);
        tableListCategory.getSelectionModel().selectFirst();
        updateCurrentCategoryToForm();
        new Thread(()-> {
            for (Category category : categoryList) {
                category.setProductList(productService.getProductsByIdCategory(category.getId()));
            }
            // update table other
            Platform.runLater(this::updateProductCurrentRowCategory);
        }).start();
    }

    private void updateProductCurrentRowCategory() {
        tableListProduct.getItems().clear();
        colNameProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        colPriceProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
        colPriceProduct.setCellValueFactory(param -> {
            Product product = param.getValue();
            return new SimpleStringProperty(FormatDouble.toString(product.getPrice()));
        });
        colDescProduct.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        colStatusProduct.setCellValueFactory(new PropertyValueFactory<Product, Integer>("status"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));
        colSupplierProduct.setCellValueFactory(new PropertyValueFactory<Product, Supplier>("supplier"));
        colCreatedAtProduct.setCellValueFactory(new PropertyValueFactory<Product, Timestamp>("createdAt"));
        colUpdatedAtProduct.setCellValueFactory(new PropertyValueFactory<Product, Timestamp>("updatedAt"));
        tableListProduct.setItems(FXCollections.observableArrayList(tableListCategory.getSelectionModel().getSelectedItem().getProductList()));
    }

    private void updateCurrentCategoryToForm() {
        Category category = tableListCategory.getSelectionModel().getSelectedItem();
        if (category == null) {
            cleanForm();
            return;
        }
        idCategory.setText(String.valueOf(category.getId()));
        nameCategory.setText(category.getName());

    }

    private void cleanForm() {
        idCategory.setText("");
        nameCategory.setText("");
        tableListCategory.getSelectionModel().clearSelection();
    }
};

    
