package com.example.abeer.quarantine.viewmodel;

public class Emp_Committe {
    Long  Employee_Id;
    Short ISAdmin;
    String FullName;
    String LoginName;
    String Password;

    public Emp_Committe(String employee_id) {

    }

    public Emp_Committe(String employee_Id, String ISAdmin, String fullName,String LoginName,String Password) {
        Employee_Id = Long.valueOf(employee_Id);
        this.ISAdmin = Short.valueOf(ISAdmin);
        FullName = fullName;
        this.LoginName =LoginName;
        this.Password =Password;

    }

    public Emp_Committe(Emp_Committe emp_committe) {
        Employee_Id = emp_committe.Employee_Id;
        this.ISAdmin = emp_committe.ISAdmin;
        FullName = emp_committe.FullName;
        this.LoginName=emp_committe.LoginName;
        this.Password=emp_committe.Password;
    }

    public Emp_Committe(String employee_id, String isAdmin, String fullName, String loginName) {
    }

    public Long getEmployee_Id() {
        return Employee_Id;
    }

    public void setEmployee_Id(Long employee_Id) {
        Employee_Id = employee_Id;
    }

    public Short getISAdmin() {
        return ISAdmin;
    }

    public void setISAdmin(Short ISAdmin) {
        this.ISAdmin = ISAdmin;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getLoginName() {
        return LoginName;
    }

    public void setLoginName(String loginName) {
        LoginName = loginName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

}
