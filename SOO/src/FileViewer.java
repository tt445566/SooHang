import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import java.io.*;
import javax.swing.event.*;

import java.util.*;

import java.text.*;

 

public class FileViewer implements TreeWillExpandListener,TreeSelectionListener,ItemListener
 {
	String combo_String="한국어";
	String pathName1;
	String title;
	String[] language = { 
			"한국어",
			"English"
	};
	
   JComboBox combobox = new JComboBox(language); 	
   
   
   JFrame frame;
   JSplitPane pMain=new JSplitPane();
   JScrollPane pLeft=null;
   JPanel pRight=new JPanel(new BorderLayout());
  //언어선택 확인 함수넣기
   
   DefaultMutableTreeNode root = new DefaultMutableTreeNode("내컴퓨터");
   JTree tree;
   
   JPanel pNorth=new JPanel();
   JPanel northText=new JPanel();
   JPanel pSouth = new JPanel();
   JLabel northLabel;
   JTextField pathText=new JTextField();
  
 FileViewer(){
	 
	 frame = new JFrame("파일 관리자"); 
	 northLabel =  new JLabel("경  로");
	 
 
	 combobox.addItemListener(new ItemListener() {
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(combobox.getSelectedItem().toString().equals("한국어")) {
			combo_String = "한국어";
			frame.setTitle( "파일 탐색기");
		 northLabel.setText("경  로");
			}
			else { 
			combo_String = "English";
			frame.setTitle("File Explore");
			northLabel.setText("Path");
			}
			
		}
	});
	 
	 
 
   init();
   start();
   frame.setSize(800,600);
   frame.setLocationRelativeTo(null);
   frame.setVisible(true);
  }

 void init(){
   String msg1;
   pMain.setResizeWeight(1);
   
   pSouth.add(combobox);
   frame.add(pSouth,BorderLayout.SOUTH);
   pathText.setPreferredSize(new Dimension(600,20));
   northText.add(northLabel);
   northText.add(pathText);
   pNorth.add(northText);
   frame.add(pNorth,BorderLayout.NORTH);
   File file=new File("");
   File list[]=file.listRoots();
   DefaultMutableTreeNode temp;
   if(combo_String=="한국어") {
	   msg1 = "없음";
   }
   else msg1 = "None";
  for(int i=0;i<list.length;++i)
   {
    temp=new DefaultMutableTreeNode(list[i].getPath());
    //언어 선택확인
    temp.add(new DefaultMutableTreeNode(msg1));
    root.add(temp);
   }
   tree=new JTree(root);
   pLeft=new JScrollPane(tree);
   
   pMain.setDividerLocation(150);
   pMain.setLeftComponent(pLeft);
   pMain.setRightComponent(pRight);
   frame.add(pMain);
  }

 void start()
  {
   tree.addTreeWillExpandListener(this);
   tree.addTreeSelectionListener(this);
  }

 public static void main(String args[]){
   JFrame.setDefaultLookAndFeelDecorated(true);
   new FileViewer();
  }

 String getPath(TreeExpansionEvent e)
  {
   StringBuffer path=new StringBuffer();
   TreePath temp=e.getPath(); 
   Object list[]=temp.getPath();
   for(int i=0;i<list.length;++i)
   {
    if(i>0)
    {
     path.append(((DefaultMutableTreeNode)list[i]).getUserObject()+"\\");
    }
   }
   return path.toString();
  }
  String getPath(TreeSelectionEvent e)
  {
   StringBuffer path=new StringBuffer();
   TreePath temp=e.getPath(); 
   Object list[]=temp.getPath();
   for(int i=0;i<list.length;++i)
   {
    if(i>0)
    {
     path.append(((DefaultMutableTreeNode)list[i]).getUserObject()+"\\");
    }
   }
   return path.toString();
  }
  
  public void treeWillCollapse(TreeExpansionEvent event){}
  
  public void treeWillExpand(TreeExpansionEvent e)
  {
   String msg2,msg3;
   if(combo_String=="한국어") {
	   msg2 = "없음";
	   msg3 = "디스크 혹은 파일을 찾을 수 없습니다.";
   }
   else {
	   msg2 = "없음";
	   msg3 = "Disk or File Not Found ";
   }
   if(((String)((DefaultMutableTreeNode)e.getPath().getLastPathComponent()).getUserObject()).equals("내컴퓨터")){}
   else
   {
    try{
     DefaultMutableTreeNode parent=(DefaultMutableTreeNode)e.getPath().getLastPathComponent();
     File tempFile=new File(getPath(e));
     File list[]=tempFile.listFiles();
     DefaultMutableTreeNode tempChild;
     for(File temp:list)
     {
      if(temp.isDirectory() && !temp.isHidden())
      {
       tempChild=new DefaultMutableTreeNode(temp.getName());
       if(true)
       {
        File inFile=new File(getPath(e)+temp.getName()+"\\");
        File inFileList[]=inFile.listFiles();
        for(File inTemp:inFileList)
        {
         if(inTemp.isDirectory() && !inTemp.isHidden())
         {
          tempChild.add(new DefaultMutableTreeNode(msg2));
          break;
         }
        }
       }
       parent.add(tempChild);
      }
     }
     parent.remove(0);
    }
    catch(Exception ex)
    {
    	//언어
     JOptionPane.showMessageDialog(frame, msg3);
    }
   }
  }
  public void valueChanged(TreeSelectionEvent e)
  {
	  String pathName;
	  if(combo_String=="한국어") {
		  pathName = "내컴퓨터";
	  }
	  else pathName = "MyComputer";
		 
   if(((String)((DefaultMutableTreeNode)e.getPath().getLastPathComponent()).getUserObject()).equals("내컴퓨터")){
   //언어
	   pathText.setText(pathName);
   }
   else
   {
    try
    {
     pathText.setText(getPath(e));
     pRight=new FView(getPath(e)).getTablePanel();
     pMain.setRightComponent(pRight);
    }
    catch(Exception ex)
    {
     
    }
   }
  }

@Override
public void itemStateChanged(ItemEvent e) {
	if(e.toString().equals("한국어")) {
		combo_String = "한국어";
	}
	else 
		combo_String = "English";
	
}



 }



 

class FView
 { 
   ATable at=new ATable();
   JTable jt=new JTable(at);
  
   JPanel pMain=new JPanel(new BorderLayout());
   JScrollPane pCenter=new JScrollPane(jt);

  File file;
   File list[];
   long size=0,time=0;

 FView(String str){
   init();
   start(str);
  }

 void init(){
   pMain.add(pCenter,"Center");
  }

 void start(String strPath)
  {
   file=new File(strPath);
   list=file.listFiles();
   at.setValueArr(list.length);
   for(int i=0;i<list.length;++i)
   {
    size=list[i].length();
    time=list[i].lastModified();
    for(int j=0;j<4;++j)
    {
     switch(j)
     {
      case 0:
       at.setValueAt(list[i].getName(),i,j);
       break;
      case 1:
       if(list[i].isFile())
        at.setValueAt(Long.toString((long)Math.round(size/1024.0))+"Kb",i,j);
       break;
      case 2:
       if(list[i].isFile())
       {
        at.setValueAt(getLastName(list[i].getName()),i,j);
       }
       else {
    	   //언어
        at.setValueAt("파일폴더",i,j);
       break;
     }
      case 3:
       at.setValueAt(getFormatString(time),i,j);
       break;
     }
    }
   }
   jt.repaint();
   pCenter.setVisible(false);
   pCenter.setVisible(true);
  }

 String getLastName(String name)
  {
   int pos=name.lastIndexOf(".");
   String result=name.substring(pos+1,name.length());
   return result;
  }
  String getFormatString(long time)
  {
   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm a");
   Date d=new Date(time);
   String temp = sdf.format(d);
   return temp;
  }
  JPanel getTablePanel()
  {
   return pMain;
  }
 }

 

class ATable extends AbstractTableModel
 {
	//언어
   String title[]={"이름", "크기", "종류","수정한 날짜"};
   String val[][]=new String[1][4];
   
  public void setValueArr(int i)
  {
   val=new String[i][4];
  }
  public int getRowCount()
  {
   return val.length;
  }
  public int getColumnCount()
  {
   return val[0].length;
  }
  public String getColumnName(int column )
  {
   return title[column];
  }
  public boolean isCellEditable(int rowIndex, int columnIndex)
  {
   if(columnIndex==0)
    return true;
   else
    return false;
  }
  public Object getValueAt(int row, int column)
  {
   return val[row][column];
  }
  public void setValueAt(String aValue, int rowIndex, int columnIndex ){
   val[rowIndex][columnIndex] = aValue;
  }
 }