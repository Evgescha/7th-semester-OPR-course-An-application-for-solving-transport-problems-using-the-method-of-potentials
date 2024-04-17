import java.util.ArrayList;

public class TestSimply2 {
    //массив с исходными данными

    // массив для поиска цикла
    static int[][] tempCros;
    static boolean next = true;
    private static int[][] arr = new int[][] { { 4,3,5,6,100 }, {8,2,4,7,200 }, {50,100,75,75,0 } };
    
    
    public static int[][] ishodnik = null;
    public static int[] potrebit = null;
    public static int[] sapasi = null;

    static int[][] opornPlan = null;
    static int[][] opornPlanTemp = null;
    
    static int[] potencPotreb = null;
    static int[] potencSclad = null;
    public static ArrayList<int[][]> STEP = new ArrayList<>();
    public static void setArr(int [][] arrT) {
        arr=arrT;
        printArr(arr);
    }
    public static void main(String[] args) {
        STEP.clear();
        initial();
        makeSbalansirowSadach();
        //initial arrCross
            printArr(ishodnik);
            printArr(potrebit);
            System.out.println();
            printArr(sapasi);
            System.out.println();
        opornPlan = new int[ishodnik.length][ishodnik[0].length];
        getMin();
        clearArrCros(); 
            printArr(opornPlan);
            System.out.println();
        opornPlanTemp = new int[opornPlan.length][opornPlan[0].length];
        copyArray(opornPlan, opornPlanTemp);
            
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!");
        int a=1;
        while(true) {
            System.out.println(a);
            a++;
            if(!findPotencial())break;
//                System.out.println();
//                printArr(potencPotreb);
//                printArr(potencSclad);
//                System.out.println();
                
            int[][] tempArr = writePotencialInArr(); 
//                printArr(tempArr);
            int[] max = findMaxBigZero(tempArr);
//                printArr(max);
            if (max[0]==-1) {
                break;
            }
            goFindCycle(max);
        }
        System.out.println("@@@@@@@@@@@@@@@@@@@@22");
               for(int[][] asd:STEP)printArr(asd);
    }       

    //разделение исходного массива на множество массивов
    static void initial() {
        //инициализация исходников
        ishodnik = new int[arr.length-1][arr[0].length-1];
        sapasi = new int[arr.length-1];
        potrebit = new int[arr[0].length-1];
        for (int i = 0; i < ishodnik.length; i++) {
            for (int j = 0; j < ishodnik[0].length ; j++) {
                ishodnik[i][j]=arr[i][j];
            }
        }
        //инициализация запасов и потребностей
        for(int i=0; i<ishodnik.length;i++)sapasi[i]=arr[i][ishodnik[0].length];
        for(int i=0; i<ishodnik[0].length;i++)potrebit[i]=arr[ishodnik.length][i];
    }
    
    
    static void makeSbalansirowSadach() {
        int sapas=0;
        int potrebn=0;
        for(int i=0; i<sapasi.length;i++)sapas+=sapasi[i];
        for(int i=0; i<potrebit.length;i++)potrebn+=potrebit[i];
        //запаса больше чем потребностей, создаем фиктивную потребность
        if(sapas>potrebn) {           
            int[][] tempishodnik = new int[ishodnik.length][ishodnik[0].length+1];
            copyArray(ishodnik, tempishodnik);        
            ishodnik = tempishodnik;
            int[] tempPotrebn = new int[potrebit.length+1];
            copyArray(potrebit, tempPotrebn);            
            tempPotrebn[tempPotrebn.length-1]=sapas-potrebn;
            potrebit = tempPotrebn;
        }
        if (sapas<potrebn) {
            int[][] tempishodnik = new int[ishodnik.length+1][ishodnik[0].length];
            copyArray(ishodnik, tempishodnik);
            ishodnik = tempishodnik;            
            int[] tempSapas = new int[sapasi.length+1];
            copyArray(sapasi, tempSapas);            
            tempSapas[tempSapas.length-1]=potrebn-sapas;
            sapasi = tempSapas;
            
        }
        potencPotreb = new int[potrebit.length];
        potencSclad = new int[sapasi.length];
    }
    

    static void getMin() {
        int[][] arrr = new int[ishodnik.length][ishodnik[0].length];
        copyArray(ishodnik, arrr);
        // инициализируем пустой минимальный элемент
        int[] findMin;
        // находим минимальный элемент в массиве
        findMin = getMinDliaSrawneniya(arrr);
        //и пока остаются минимальные элементы, повторяем одни и те же действия
        do {
            //инициализируем доступный минимальный элемент
            int iMin = findMin[0], jMin = findMin[1], min = findMin[2];

            // сравниваем доступный элемент с остальными, для поиска минимального
            if (iMin != -1 || jMin != -1) {
                for (int i = 0; i < arrr.length; i++) {
                    for (int j = 0; j < arrr[0].length; j++) {
                        // если найли элемент, меньше найденного ранее и больше чем 0(пока у нас закрытая задача), сохраняем
                        if (opornPlan[i][j] == 0 && arrr[i][j] < min && arrr[i][j] > 0) {
                            iMin = i;
                            jMin = j;
                            min = arrr[i][j];
                        }
                    }

                }
            }
            //как только нашли минимальный элемент, вычитываем потребности/возможности
            step(new int[] { iMin, jMin, min });
            // смотрим, есть ли еще минимальные  элементы
            findMin = getMinDliaSrawneniya(arrr);
        } while (findMin[0] != -1);
        
        //делаем то же самое, что и выше, только при условии, что у нас открытая задача
        findMin = getMinDliaSrawneniyaWithZero(arrr);
        do {
            int iMin = findMin[0], jMin = findMin[1], min = findMin[2];
            // сравниваем любой доступный элемент с остальными, для поиска минимального
            if (iMin != -1 || jMin != -1) {
                for (int i = 0; i < arrr.length; i++) {
                    for (int j = 0; j < arrr[0].length; j++) {
                        if (opornPlan[i][j] == 0 && arrr[i][j] < min) {
                            iMin = i;
                            jMin = j;
                            min = arrr[i][j];
                        }
                    }

                }
            }
            step(new int[] { iMin, jMin, min });
            findMin = getMinDliaSrawneniya(arrr);
        } while (findMin[0] != -1);
        
    }
    static int[] getMinDliaSrawneniya(int[][] arrr) {
        //инициализируем отрицательные значения, как если бы мы ничего не нашло
        int iMin = -1, jMin = -1, min = -1;
        // проходим по всему массиву
        for (int i = 0; i < opornPlan.length; i++) {
            for (int j = 0; j < opornPlan[0].length; j++) {
                //если текущий элемент доступен для сравнения с другими и не равен 0(на случай открытой задачи), берем его координаты и значение
                if (opornPlan[i][j] == 0 && arrr[i][j] > 0) {
                    iMin = i;
                    jMin = j;
                    min = arrr[i][j];
                    return new int[] { iMin, jMin, min };
                }
            }

        }
        return new int[] { iMin, jMin, min };

    }
    
    static int[] getMinDliaSrawneniyaWithZero(int[][] arrr) {
        //инициализируем отрицательные значения, как если бы мы ничего не нашло
        int iMin = -1, jMin = -1, min = -1;
        // проходим по всему массиву
        for (int i = 0; i < opornPlan.length; i++) {
            for (int j = 0; j < opornPlan[0].length; j++) {
                //если текущий элемент доступен для сравнения с другими и  равен 0(открытая задача), берем его координаты и значение
                if (opornPlan[i][j] == 0 ) {
                    iMin = i;
                    jMin = j;
                    min = arrr[i][j];
                    return new int[] { iMin, jMin, min };
                }
            }

        }
        return new int[] { iMin, jMin, min };

    }
    // вычитание возможностей/потребностей
    static void step(int[] min) {
        //инициализируем координаты минимального элемента, и координаты возможностей/потребностей
        int i = min[0];
        int j = min[1];

        // находим минимальную возможность/потребность
        if(min[0]!=-1) {
            int minus = Math.min(sapasi[i], potrebit[j]);
            // вычитаем ее из столбца возможность и потребность
            sapasi[i] = sapasi[i] - minus;
            potrebit[j] = potrebit[j] - minus;
            //помечаем ячейку как измененную
            opornPlan[i][j] = minus;
    
            
            // убираем ненужные строки или столбцы, елси закончились возможности/потребности
            if (sapasi[i] == 0) {
                for(int k=0; k< opornPlan[0].length;k++) {
                    if(opornPlan[i][k]==0)opornPlan[i][k]=-1;
                }
            }
            else
            if (potrebit[j] == 0) {
                for(int k=0; k< opornPlan.length;k++) {
                    if(opornPlan[k][j]==0)opornPlan[k][j]=-1;
                }
            }
        //выводим результат на экран
//        System.out.println("min="+min[2]);
//        System.out.println("minus="+minus);        
        printArr(ishodnik);
        printArr(opornPlan);
        }
    }
    static void clearArrCros() {
        for (int i = 0; i < opornPlan.length; i++) {
            for (int j = 0; j < opornPlan[0].length; j++) {
                if(opornPlan[i][j]== -1) opornPlan[i][j]=0;
            }
        }
    }
    // распечатать массив
    static void printArr(int[][] arrr) {
        System.out.println();
        for (int i = 0; i < arrr.length; i++) {
            for (int j = 0; j < arrr[0].length; j++) {
                System.out.print(arrr[i][j] + "   \t");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    static void printArr(int arrr[]) {
        for(int i=0; i<arrr.length;i++)System.out.print(arrr[i]+" \t");
    }

    static boolean findPotencial(){
     // уравнения для нахождения потенциалов
        int[][] Cn = new int[opornPlan.length+opornPlan[0].length][opornPlan.length + opornPlan[0].length];
        
        // Ответ СЛАУ, Cn их строки, Cn из толбца
        int[] bi = new int[Cn.length];
//        potencials = bi;
        int current = 0;
        printArr(opornPlan);
        
        
        int[][] STEPTEMP = new int[opornPlan.length][opornPlan[0].length];
        copyArray(opornPlan, STEPTEMP);
        
        
        
      STEP.add(STEPTEMP);
      
      
      
      
      
        int[] pot = null;
        for(int c0=0; c0<Cn[0].length;c0++) {
                for(int i=0; i<ishodnik.length;i++) {
                    for(int j=0; j<ishodnik[0].length;j++) {
                       if(opornPlan[i][j]>0) {
                          bi[current]=ishodnik[i][j];
                          Cn[current][j]=1;
                          Cn[current][ishodnik[0].length+i]=1;
                          current++;
                       }
                    }
                }
                Cn[current][c0]=1;
//                printArr(Cn);
//                printArr(bi);
                pot = gaus(bi,Cn); 
                //printArr(pot);
                current = 0;
                if (pot!=null) {
                    for(int i=0; i<ishodnik[0].length;i++) {potencPotreb[i]=pot[current];current++;}
                    for(int i = 0; i<ishodnik.length;i++) {potencSclad[i]=pot[current];current++;}
                    int[][] tempArr = writePotencialInArr(); 
                    
                    if(checkPotencoal(tempArr)) {
                        break;
                    }
                   
                }
                    
        }
        if (pot==null)return false;
        return true;
    }
    static boolean checkPotencoal(int[][]tempArr) {
        for(int i=0; i<tempArr.length;i++) {
            for(int j=0; j<tempArr[0].length;j++) {
                if(opornPlan[i][j]>0 && tempArr[i][j]!=0)return false;
            }
          }
        return true;
    }
    
    static int[] gaus(int[] bi, int[][] urawn) {
        /* Ввод данных */
//        Scanner s = new Scanner(System.in);
        int n = bi.length;
        int m = bi.length;
        int[][] A = urawn;
        int[] b = bi;
       
        /* Метод Гаусса */
        int N  = n;
        for (int p = 0; p < N; p++) {

            int max = p;
            for (int i = p + 1; i < N; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            int[] temp = A[p]; A[p] = A[max]; A[max] = temp;
            int   t    = b[p]; b[p] = b[max]; b[max] = t;

            if (Math.abs(A[p][p]) <= 1e-10) {
//                System.out.println("NO");
                return null;
            }

            for (int i = p + 1; i < N; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < N; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        // Обратный проход

        int[] x = new int[N];
        for (int i = N - 1; i >= 0; i--) {
            int sum = 0;
            for (int j = i + 1; j < N; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }

        /* Вывод результатов */

        if (n < m) {
//            System.out.print("INF");
        } else {
//            System.out.println("YES");
            for (int i = 0; i < N; i++) {
//                potencials[i]=x[i];
//                System.out.print(x[i] + " ");
            }
        }
        return x;
    
    }
   
    static void copyArray(int[][] from, int[][] to) {
        for(int i=0; i<from.length;i++) {
            for(int j=0; j<from[0].length;j++) {
                to[i][j]=from[i][j];
            }
          }
    }
    static void copyArray(int[] from, int[] to) {
        for(int i=0; i<from.length;i++) {
                to[i]=from[i];            
          }
    }
    static void copyArray(int[][] from, int[][] to,int rowCount, int collCount) {
        for(int i=0; i<rowCount;i++) {
            for(int j=0; j<collCount;j++) {
                to[i][j]=from[i][j];
            }
          }
    }
   
    
    
    static int[][] writePotencialInArr() {
        //временный массив исходных элементов для проверки потенциалов
        int[][] tempArr = new  int[ishodnik.length][ishodnik[0].length];
        copyArray(ishodnik, tempArr);
        for(int i=0; i<tempArr.length;i++) {
            for(int j=0; j<tempArr[0].length;j++) {
                tempArr[i][j]=potencPotreb[j]+potencSclad[i]-ishodnik[i][j];
            }
          }
//        printArr(tempArr);
        return tempArr;
       
    }
    
    static int[] findMaxBigZero(int[][] array) {
        int max = -1, imax=-1,jmax=-1;
        for(int i=0; i<array.length;i++) {
            for(int j=0; j<array[0].length;j++) {
                if(array[i][j]>0 && array[i][j]>max) {
                    max = array[i][j];
                    imax=i;
                    jmax=j;
                }
            }            
        }
        return new int[] {imax,jmax,max};
    }
    
    static void goFindCycle(int[]start){
        int i=start[0], j=start[1];
        tempCros=new int[opornPlan.length][opornPlan[0].length];
        copyArray(opornPlan, tempCros);
        tempCros[i][j]=-1;      
        //точки зацикленного цикла
        int[] points   =        findLeftDown(i,j);
        if(points[0]==-1)points=findLeftUp(i,j);
        if(points[0]==-1)points=findRightDown(i,j);
        if(points[0]==-1)points=findRightUp(i,j);
        tempCros[i][j]=0;
        //find min
//        printArr(tempCros);
//        printArr(points);
        if(points[0]!=-1) {
            int min = tempCros[points[2]][points[3]];            
            if(tempCros[points[2]][points[3]]>tempCros[points[6]][points[7]])min =tempCros[points[6]][points[7]]; 

            System.out.println();
            boolean add = true;
            // add and remove min elem
            for(int point=0; point<points.length;point+=2) {
                if(add) {
                    tempCros[points[point]][points[point+1]]+=min; 
                    }
                else {
                    tempCros[points[point]][points[point+1]]-=min;
                    }
                add=add?false:true;
            }
//            System.out.println();
//            printArr(opornPlan);
//            System.out.println();
            copyArray(tempCros, opornPlan);
//            printArr(tempCros);
//        System.out.println();
        } else next=false; 
    }
    
    static int[]  findLeftDown(int i, int j) {
        try {
        System.out.println("find left down");
        for(int collLeft=j-1; collLeft>=0;collLeft--) {
            if(tempCros[i][collLeft]>0) {
                //find down
                for(int rowDown=i+1; rowDown<tempCros.length;rowDown++) {
                    if(tempCros[rowDown][collLeft]>0) {
                        //find right
                        for(int collRight=collLeft+1; collRight>=0;collRight--) {
                            if(tempCros[rowDown][collRight]>0) {
                                //find up finnaly
                                for(int rowUp=rowDown-1;rowUp>=0;rowUp--) {
                                    if(tempCros[rowUp][collRight]==-1 && i==rowUp && j==collRight) {
                                        System.out.println("i:"+i +",j:"+j +";  "
                                                         + "i:"+i +",k:"+collLeft +";  "
                                                         + "k:"+rowDown +",l:"+collLeft +";  "
                                                         + "l:"+rowDown +",o:"+collRight);
                                        return new int[] {
                                                i,j,
                                                i,collLeft,
                                                rowDown, collLeft,
                                                rowDown,collRight};
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }}catch(Exception ex) {}
        return new int[] {-1};
    }
    static int[]  findRightDown(int i, int j) {
        System.out.println("find right down");
        try {
        for(int collRight=j+1; collRight<tempCros[0].length;collRight++) {
            if(tempCros[i][collRight]>0) {
                //find down
                for(int rowDown=i+1; rowDown<tempCros.length;rowDown++) {
                    if(tempCros[rowDown][collRight]>0) {
                        //find left
                        for(int collLeft=collRight-1; collLeft>=0;collLeft--) {
                            if(tempCros[rowDown][collLeft]>0) {
                                //find up finnaly
                                for(int rowUp=rowDown-1;rowUp>=0;rowUp--) {
                                    if(tempCros[rowUp][collLeft]==-1 && i==rowUp && j==collLeft) {
                                        System.out.println("i:"+i +",j:"+j +";  "
                                                         + "i:"+i +",k:"+collRight +";  "
                                                         + "k:"+rowDown +",l:"+collRight +";  "
                                                         + "l:"+rowDown +",o:"+collLeft);
                                        return new int[] {
                                                i,j,
                                                i,collRight,
                                                rowDown, collRight,
                                                rowDown,collLeft};
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }}catch(Exception ex) {}
        return new int[] {-1};
    }     
    static int[]  findLeftUp(int i, int j) {
        System.out.println("find left up");
        try {
        for(int collLeft=j-1; collLeft>=0;collLeft--) {
            if(tempCros[i][collLeft]>0) {
                //find up
                for(int rowUp=i+1; rowUp>=0;rowUp--) {
                    if(tempCros[rowUp][collLeft]>0) {
                        //find right
                        for(int collRight=collLeft+1; collRight<tempCros[0].length;collRight++) {
                            if(tempCros[rowUp][collRight]>0) {
                                //find up finnaly
                                for(int rowDown=rowUp+1;rowDown<tempCros.length;rowDown++) {
                                    if(tempCros[rowDown][collRight]==-1 && i==rowDown && j==collRight) {
                                        System.out.println("i:"+i +",j:"+j +";  "
                                                         + "i:"+i +",k:"+collLeft +";  "
                                                         + "k:"+rowUp +",l:"+collLeft +";  "
                                                         + "l:"+rowUp +",o:"+collRight);
                                        return new int[] {
                                                i,j,
                                                i,collLeft,
                                                rowUp, collLeft,
                                                rowUp,collRight};
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }}catch(Exception ex) {}
        return new int[] {-1};
    }
    static int[]  findRightUp(int i, int j) {
        System.out.println("find right up");
        try {
        for(int collRight=j+1; collRight<tempCros[0].length;collRight++) {
            if(tempCros[i][collRight]>0) {
                //find up
                for(int rowUp=i-1; rowUp>=0;rowUp--) {
                    if(tempCros[rowUp][collRight]>0) {
                        //find left
                        for(int collLeft=collRight-1; collLeft>=0;collLeft--) {
                            if(tempCros[rowUp][collLeft]>0) {
                                //find up finnaly
                                for(int rowDown=rowUp+1;rowDown<tempCros.length;rowDown++) {
                                    if(tempCros[rowDown][collLeft]==-1 && i==rowDown && j==collLeft) {
                                        System.out.println("i:"+i +",j:"+j +";  "
                                                         + "i:"+i +",k:"+collRight +";  "
                                                         + "k:"+rowUp +",l:"+collRight +";  "
                                                         + "l:"+rowUp +",o:"+collLeft);
                                        return new int[] {
                                                i,j,
                                                i,collRight,
                                                rowUp, collRight,
                                                rowUp,collLeft};
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }}catch(Exception ex) {}
        return new int[] {-1};
    }
    
    
}
