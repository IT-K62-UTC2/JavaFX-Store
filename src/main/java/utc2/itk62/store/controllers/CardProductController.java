package utc2.itk62.store.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utc2.itk62.store.models.InvoiceDetail;
import utc2.itk62.store.models.Product;
import utc2.itk62.store.util.FormatDouble;

public class CardProductController {
    public Button btnAdd;
    public Label priceProduct;
    public ImageView imageProduct;
    public Label nameProduct;
    public Spinner<Integer> cashQuantity;
    public Label quantity;
    public Label descProduct;
    public Label idProduct;

    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        idProduct.setText(String.valueOf(product.getId()));
        descProduct.setText(product.getDescription());
        priceProduct.setText(FormatDouble.toString(product.getPrice()));
        nameProduct.setText(String.valueOf(product.getName()));
        try {
            imageProduct.setImage(new Image(product.getAvatar()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        quantity.setText(String.valueOf(product.getQuantity()));
        cashQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, product.getQuantity(), 0));
    }

    public void initialize() {}

    public void setBtnAdd(SellController saleController) {
        btnAdd.setOnAction(actionEvent -> {
            if(cashQuantity.getValue() <=0) {
                return;
            }

            for(InvoiceDetail item : saleController.listOrders) {
                if(item.getProduct().getId() == product.getId()) {
                    item.setProductQuantity(item.getProductQuantity() + cashQuantity.getValue());
                    item.setMoneyTotal(item.getMoneyTotal() + product.getPrice() * cashQuantity.getValue());
                    quantity.setText(String.valueOf(Integer.valueOf(quantity.getText()) - cashQuantity.getValue()));
                    cashQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.valueOf(quantity.getText()), 0));
                    saleController.tableViewOrder.refresh();
                    return;
                }
            }

            InvoiceDetail invoiceDetail = new InvoiceDetail();
            invoiceDetail.setProduct(product);

            invoiceDetail.setMoneyTotal(product.getPrice() * cashQuantity.getValue());
            invoiceDetail.setProductQuantity(cashQuantity.getValue());

            quantity.setText(String.valueOf(Integer.valueOf(quantity.getText()) - cashQuantity.getValue()));
            cashQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.valueOf(quantity.getText()), 0));
            saleController.listOrders.add(invoiceDetail);

        });
    }

}
