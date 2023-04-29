package utc2.itk62.sneaker.services;

import utc2.itk62.sneaker.models.InvoiceDetail;
import utc2.itk62.sneaker.repositories.InvoiceDetailRepo;

import java.util.List;

public class InvoiceDetailsService {
    private static final InvoiceDetailRepo invoiceDetailRepo = new InvoiceDetailRepo();

    public boolean createInvoiceDetail(InvoiceDetail invoiceDetail) {
        if(invoiceDetailRepo.createInvoiceDetail(invoiceDetail) <= 0) {
            return false;
        }
        return true;
    }

    public List<InvoiceDetail> getInvoiceDetailByIdInvoice(int id) {
        return invoiceDetailRepo.getAllInvoicesDetailByInvoiceId(id);
    }
}