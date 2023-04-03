package net.openio.jrocksDb.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Status <K>{
     private boolean success;

     private String message;

     K date;

     private boolean hasValue;


     public static Status success(){
          Status status=new Status();
          status.success=true;
          return status;
     }



     public static Status NOTColumnFamilyHandle(String ColumnFamilyHandle){
          Status status=new Status();
          status.success=false;
          status.message="not have "+ColumnFamilyHandle+" ColumnFamilyHandle";
          return status;
     }

     public static Status<Value> Get(Value date){
          Status<Value> status=new Status<>();
          status.success=true;
          status.hasValue=true;
          status.date=date;
          return status;
     }

     public static Status<ColumnFamily> ColumnFamily(ColumnFamily date){
          Status<ColumnFamily> status=new Status<>();
          status.success=true;
          status.hasValue=true;
          status.date=date;
          return status;
     }

     public static Status<List<ColumnFamily>> ColumnFamily(List<ColumnFamily> date){
          Status<List<ColumnFamily>> status=new Status<>();
          status.success=true;
          status.hasValue=true;
          status.date=date;
          return status;
     }

     public static Status<Value> GetDeleteValue(){
          Status<Value> status=new Status<>();
          status.success=true;
          status.hasValue=false;
          status.date=null;
          return status;
     }

     public static Status CKeyTypeVerify(){
          Status<Value> status=new Status<>();
          status.success=false;
          status.hasValue=false;
          status.date=null;
          status.message="key type is wrong";
          return status;
     }

     public static Status CValueTypeVerify(){
          Status<Value> status=new Status<>();
          status.success=false;
          status.hasValue=false;
          status.date=null;
          status.message="key type is wrong";
          return status;
     }

     public static Status hasValue(){
          Status<Value> status=new Status<>();
          status.success=false;
          status.hasValue=false;
          status.date=null;
          status.message="key has init";
          return status;
     }

     public static Status NotHasValue(){
          Status<Value> status=new Status<>();
          status.success=false;
          status.hasValue=false;
          status.date=null;
          status.message="key not init";
          return status;
     }

     public static Status put(){
          Status<Value> status=new Status<>();
          status.success=true;
          status.hasValue=false;
          status.date=null;
          return status;
     }

     public static Status<Value> update(Value value){
          Status<Value> status=new Status<>();
          status.success=true;
          status.hasValue=false;
          status.date=null;
          return status;
     }

     public static Status<Value> Delete(Value value){
          Status<Value> status=new Status<>();
          status.success=true;
          status.hasValue=false;
          status.date=null;
          return status;
     }

}
