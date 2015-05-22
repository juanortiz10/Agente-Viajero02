package agenteviajero02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Model {
  StringBuilder sb = new StringBuilder();
  int counter=0,sizeTour=0;
  int idxtext = -1;
  int[][] array;
  ArrayList<ArrayList> matrix;
  int[][] arrayRecovery;
  int[]suma;
  ArrayList road= new ArrayList<Integer>();
  ArrayList keys= new ArrayList<Integer>();
  HashMap map= new HashMap<Integer,Integer>();
  //This method reads the file .txt, then is saved in a String Builder
  public void readDocument(){
      BufferedReader br = null;

    try {

     String linea;

     br = new BufferedReader(new FileReader("gr17.tsp"));

     while ((linea= br.readLine()) != null) {
      if (linea.compareTo("EDGE_WEIGHT_SECTION")==0){
        idxtext = counter+1;
      }
      if (linea.compareTo("EOF")==0){
        break;
      }
      sb.append(linea+"\n");
      counter++;
    }

  } catch (Exception e) {
   e.printStackTrace();
 } finally {
   try {
    if (br != null)br.close();
  } catch (Exception ex) {
    ex.printStackTrace();
  }
  String[] values = sb.toString().split("\n");

  ArrayList<String> list = new ArrayList<>();

  for (int i = idxtext; i < values.length; i++) {
    list.add(values[i]);
  }

  matrix = new ArrayList<>();

  ArrayList<Integer> toinsert= new ArrayList<>();

  for (String string : list) {
    String[] temp=string.split("\\s+");

    for (String inner : temp) {
      if(inner.isEmpty())
        continue;
      Integer number = Integer.parseInt(inner);
      if(number==0){

        toinsert.add(number);
        matrix.add(toinsert);
        toinsert = new ArrayList<>();
      }else{
        toinsert.add(number);
      }
    }
  }

  for (int i = 0; i < matrix.size(); i++) {
    for (int j = 0; j < matrix.size(); j++) {
      if (j>=i){
        Integer number = (Integer)matrix.get(j).get(i);
        if(number!=0)
          matrix.get(i).add(number);
      }
    }
  }


}
  }
  //This method saves the file's information in a matrix
  public void generateArray(){
      array= new int[matrix.size()][matrix.size()];
      arrayRecovery=new int[matrix.size()][matrix.size()];
      for(int i =0; i < matrix.size(); i++){
        for(int j =0; j <matrix.size(); j++){
            array[i][j]= (Integer)matrix.get(i).get(j);
            arrayRecovery[i][j]= (Integer)matrix.get(i).get(j);
        }
      }
}

  //This method chooses the first road which is going to start the heuristic
  public int generateFirstRoad(){
      int var=counter-2;
      return (0 + (int)(Math.random()*var));
  }

  //This method is going to search the best next route
  public void searchRoad(){
      int min=1000,value = 0,position=0,search;
      if(road.size()>1){
        for(int i=0; i<road.size(); i++){
            search=(int) road.get(i);
            checkNextRoad(search);
        }
        compareAndSelect();
        keys.clear();
        map.clear();
      }else{
          for(int i=0; i<array.length;i++){
              int index=(int) road.get(0);
              value=array[index][i];
               if(value<min && value!=0){
                 min=value;
                 position=i;
               }
          }
          road.add(position);
          road.add(road.get(0));
          for(int j=0; j<array.length; j++){
              for(int k=0;k<array.length; k++){
               if(k==position)
                   array[j][k]=0;
              }
          }
      }
  }
//This Method calls searchRoad method to asks about next roads until it has found all of them
  public void generateNextRoad(int firstRoad){
      road.add(firstRoad);

        for(int j=0; j<array.length; j++){
              for(int k=0;k<array.length; k++){
               if(k==firstRoad)
                   array[j][k]=0;
              }

          }
      for(int i=0; i<counter;i++){
      searchRoad();
      }
  }

  //This method checks all available roads able to be selected
  public void checkNextRoad(int numberToCheck){
    int value=0,min=1000,stop=0;
    for(int i=0; i<array.length;i++){
        value=array[numberToCheck][i];
        if(value<min && value!=0){
            min=value;
        }
    }
    map.put(numberToCheck, min);
    keys.add(numberToCheck);
  }
  //This method compares all the available roads and select the best one
  public void compareAndSelect(){
      int value=1000,best=0,finalKey=0;
      for(int i=0; i<map.size(); i++){
          int a=(int) map.get(keys.get(i));
          int key=(int) keys.get(i);
            if(a<value){
              value=a;
              finalKey=key;
              best=value;
          }
      }
          int selected=0;
          for(int k=0; k<array.length; k++){
              int rand=array[finalKey][k];

              if(rand==best && rand !=0){
                  selected=k;
                  selectArch(k);
                  break;
              }
          }
           for(int j=0; j<array.length; j++){
              for(int k=0;k<array.length; k++){
               if(k==selected){
                   sizeTour+=array[j][k];
                   array[j][k]=0;
                 }
              }

          }
  }

  //This function selects the best option to insert the road, in which arch PROBLEM FOUNDED HERE
  public void selectArch(int roadToAdd){
      suma=new int[counter];
      try{
      int val1,val2,sumas=0;
      for(int i=0; i<road.size(); i++){

          val1=(int)road.get(i);
          val2=(int)road.get(i+1);
          sumas=(arrayRecovery[val1][roadToAdd])+(arrayRecovery[roadToAdd][val2])-(arrayRecovery[val1][val2]);
          suma[i]=sumas;
          }
      }catch(Exception ex){}
      road.add(wherePut(),roadToAdd);
  }
  public int wherePut(){
      int min=1000,position = 0;
      for(int i=0; i<suma.length; i++){
          if(suma[i]<min){
              min=suma[i];
              position=i;
          }
      }
      return position;
  }
}
