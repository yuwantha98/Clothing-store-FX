package repository;

import repository.service.impl.*;

public class DaoFactory {
    private static DaoFactory daoFactory;
    private DaoFactory(){}

    public enum DaoType{
        PRODUCT,EMPLOYEE, ORDER, ORDERDETAIL, SALESREPORT, SUPPLIER
    }

    public static DaoFactory getDaoFactory(){
        return daoFactory!=null?daoFactory:(daoFactory=new DaoFactory());
    }

    public <T>T getDaoType(DaoType type){
        switch (type){

            case PRODUCT:
                return (T)new ProductDaoImpl();
            case EMPLOYEE:
                return (T) new EmployeeDaoImpl();
            case SUPPLIER:
                return (T) new SupplierDaoImpl();
            case ORDER:
                return (T) new OrderDaoImpl();
            case ORDERDETAIL:
                return (T) new OrderDetailDaoImpl();
            case SALESREPORT:
                return (T) new SalesReportDaoImpl();
            default:
                throw new IllegalArgumentException("Invalid DAO type");
        }
    }
}

