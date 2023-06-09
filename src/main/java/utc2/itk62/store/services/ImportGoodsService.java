package utc2.itk62.store.services;

import utc2.itk62.store.common.FromAndToDate;
import utc2.itk62.store.models.ImportGoods;
import utc2.itk62.store.repositories.ImportGoodsRepo;

import java.util.List;

public class ImportGoodsService {
    private static final ImportGoodsRepo importGoodsRepo = new ImportGoodsRepo();

//    public List<ImportGoods> getAllImportGoods() {
//        return  importGoodsRepo.getAll();;
//    }

    public ImportGoods createImportGoods(ImportGoods importGoods){
        return importGoodsRepo.createImportGoods(importGoods);
    }


    public List<ImportGoods> getAllImportGoods(FromAndToDate fromAndToDate) {
        return importGoodsRepo.getAllImportGoods(fromAndToDate);
    }
}
