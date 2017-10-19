package finalassignment;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;


public class Manager implements ActionListener{
	
		private JLabel first = new JLabel(); //欢饮界面
		private Icon pic = new ImageIcon("./hello.jpg");//欢饮界面的图片
		
		static private Connection c = null; //与数据库的连接c
		static private Statement stmt = null;//stmt 用于执行各种sql语句
		private JFrame f = null;
		
		private JTabbedPane tp = new JTabbedPane(); //主面板 
		private JPanel panel = new JPanel();
		private JTable table[] = new JTable[8];//因为数据库里建立了8张表，所以这里有8张
		private JButton delete[] = new JButton[8],insert[] = new JButton[8],execute = new JButton("执行");//每个表对应一个删除按钮一个插入按钮
		//execute 用于执行SQL语句的面板
		private JMenuItem item[] = new JMenuItem[8],out = new JMenuItem("退出"),
				EXSQL = new JMenuItem("sql查询"),instruction = new JMenuItem("图片管理指导");
		//一共有8个菜单元件，对应八张表，另外三个的功能见代码
		private JTextField textarea[][] ={//
				new JTextField[3],
				new JTextField[7],
				new JTextField[7],
				new JTextField[6],
				new JTextField[3],
				new JTextField[7],
				new JTextField[3],
				new JTextField[4]
		};
		//textarea 用来读入要插入的元素，因为不同的表列个数不同，所以数组大小不同
		private JTextArea SQL = new JTextArea(30,30);//执行SQL语句的区域，详细作用请见后面
		
		static final String headline[][]={//创建表格时的表头
				
			{"ID","城市名称","简单介绍"},
			{"ID","所在城市","名称","介绍","开始时间","结束时间","价格"},
			{"ID","名称","所在城市","地址","类型","价格","联系方式"},
			{"ID","名字","具体介绍","价格","性别","所在城市"},
			{"ID","交通工具名称","介绍"},
			{"ID","起点","终点","类型","出发","到达","价格"},
			{"ID","用户名","密码"},
			{"ID","用户","名称","内容"}
		};
		
		static private String type[]={//表的名字，执行SQL语句用
				 "ALLCITY","PROJECTS","HOTELS","GUIDES","MEDIA","TRANSPORT","USERS","ROUTE"
		  };
		  
	  	private static void GetConnection(){ //连接数据库
			  
			  try{
				  Class.forName("org.sqlite.JDBC");
			      c = DriverManager.getConnection("jdbc:sqlite:mydata.db");
			      stmt = c.createStatement();
			      stmt.execute("PRAGMA foreign_keys = ON;");
			  }
			  catch(Exception e){
				  System.out.println("failed");
			  }
		  }
	  	
		  public static ResultSet ExecuteSQL(String sql){ //执行任意一个SQL语句，返回resultset
			  
			  ResultSet rs = null;
			  try{
				  GetConnection();
				  rs = stmt.executeQuery(sql);  //执行sql
			  }catch(Exception e){
			  }
			 return rs;
		  }
		  
		  public static boolean DeleteByID(String type,int ID){ //获取这一行的ID去数据库中删除这一行
			  
			  try{
				  GetConnection();
				  String sql = "delete from "+type+" where ID="+"\""+(ID)+"\";";
				  stmt.executeUpdate(sql);
				  return true;
			  }catch(Exception e){
				  TOOL.ReportError(e); //执行sql
				  return false;
			  }
		  }
		  
		  public static boolean Insert(String type,String set[]){//插入，type是插入的表明，set是插入元素的集合
			  
			    try {
			      GetConnection();
			      String sql="INSERT INTO "+type+" VALUES(";
			      sql+="'"+set[0]+"'";
			      for(int i=1;i<set.length;i++){
			    		 sql+=",'"+set[i]+"'";
			      }
			      sql+=");";
			      //以上构用set建了一条sql语句，insert into 表名 values(拆分set形成的一条语句);
			      stmt.executeUpdate(sql);
			      return true;
			    } catch ( Exception e ) {
			      return false;
			    }	
		  }
		  
		  public static boolean Login(String a,char pw[]){ 
		 //根据用户查询一个密码，看看是否与传入的密码是否匹配 用于登录
			  
			  boolean flag = false;
			  try{
				  String sql = "SELECT PASSWORD FROM USERS WHERE USERNAME='"+a+"';";
				  char[] getpw = null;
				  GetConnection();
				  ResultSet rs = stmt.executeQuery(sql);
				  while(rs.next()){
					  getpw = rs.getString(1).toCharArray();
				  }
				  if(Arrays.equals(pw, getpw))flag = true;
			  }catch(Exception e){
				  TOOL.ReportError(e);
			  }
			  return flag;
		  }
		  
		  public static boolean Register(String a,String pw){
			  //使用Insert函数来插入一个新的用户数据，ID为用户名的哈希码
			  boolean flag = false;
			  try{
				  String set[]={
					a.hashCode()+"",a,pw	  
				  };
				  if(Insert("USERS",set))flag = true;
			  }catch(Exception e){
				 TOOL.ReportError(e); 
			  }
			  return flag;
		  }
		  
		  public static ResultSet GetTable(String type){
			  //获得一整张表，返回resultset
			  try {
				  GetConnection();
			      stmt = c.createStatement();
			      ResultSet rs = stmt.executeQuery( "SELECT * FROM "+type+";" );
			      return rs;
			  }
			  catch(Exception e){
				  TOOL.ReportError(e);
				  return null;
			  }
		  }
		  
		  public static JTable CreateTable(ResultSet rs,String headline[]){
			//用一个查询结果来构建一张JTable并返回它
				int column;
				DefaultTableModel b = null;
				JTable table = null;
				//data用来保存数据
				Vector<Vector<Object>> data = new Vector<Vector<Object>>();
				//columnname是表头，使用上面的headline来构建
				Vector<Object> columnname  = new Vector<Object>();
				columnname.addAll(Arrays.asList(headline));
				try{
					//循环知道rs没有数据，将数据装入data
					 column = rs.getMetaData().getColumnCount();//获得列数
					 while(rs.next()){
					   Vector<Object> a = new Vector<Object>();
					   for(int i=0;i<column;i++){
					    		a.add(rs.getObject(i+1));
					   	}
					   data.add(a);
					  }
				}catch(Exception e){
					TOOL.ReportError(e);
					return null;
				}
				//构建一张表并返回
				b = new DefaultTableModel(data,columnname);
				table = new JTable(b);
				table.setModel(b);
				table.setRowHeight(30);
				return table;
			}
		  
		  private JPanel CreatePanel(String type[],int i){
			  //构建一个插入面板，这个面板用来获取插入的信息
			  JPanel a = new JPanel();
			  int length = type.length;
			  a.setLayout(new BoxLayout(a,BoxLayout.Y_AXIS));
			  for(int k=0;k<length;k++){
				  JLabel text = new JLabel(type[k]);
				  JTextField area = new JTextField();
				  textarea[i][k] = area;//外部变量
				  a.add(text);
				  a.add(area);
			  }
			  
			  return a;
		  }
		  
		  public void CreateInterface(){
			  //整个界面的创建
			  f = new JFrame("数据库管理系统");
			  Toolkit kit=Toolkit.getDefaultToolkit();
			  Dimension screenSize=kit.getScreenSize();
			  first.setIcon(pic);
			  f.add(panel);
			  panel.add(tp);
			  panel.add(first);
			  JMenuBar menubar1 = new JMenuBar();
			  f.setJMenuBar(menubar1);
			  JMenu menu1=new JMenu("数据管理");
			  JMenu menu2=new JMenu("其他操作");
			  menubar1.add(menu1);
			  menubar1.add(menu2);
		
			  item[0]= new JMenuItem("城市数据管理");
			  item[1]=new JMenuItem("景点数据管理");
			  item[2]=new JMenuItem("旅馆数据管理");
			  item[3]=new JMenuItem("导游数据管理");
			  item[4]=new JMenuItem("交通工具管理");
			  item[5]=new JMenuItem("交通数据管理");
			  item[6]=new JMenuItem("用户管理");
			  item[7]=new JMenuItem("用户行程记录");
			  
			  
			  for(int i=0;i<8;i++){//
				  menu1.add(item[i]);
				  delete[i] = new JButton("删除");
				  delete[i].addActionListener(this);
				  insert[i] = new JButton("添加");
				  insert[i].addActionListener(this);
			  }
			  execute.addActionListener(this);
			
			  menu2.add(out);
			  menu2.add(EXSQL);
			  menu2.add(instruction);
			  
			  //为每个item添加监听事件
			  for(int i=0;i<8;i++){//
				  
				  final int num = i;
				  //这个表对应插入和删除按键
				  final JButton deletion = delete[i];
				  final JButton insertion = insert[i];
				  //创建这个表对应的插入面版，装到scrollpane里
				  final JScrollPane b = new JScrollPane(CreatePanel(headline[i],i));
				  //添加监听事件
				  item[i].addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent e) {
							
							table[num] = CreateTable(GetTable(type[num]),headline[num]);//
							JScrollPane a = new JScrollPane(table[num]);
							JPanel p1 = new JPanel();
							JPanel p2 = new JPanel();
							p1.setLayout(new BorderLayout());
							p2.setLayout(new BorderLayout());
							p1.add(a,BorderLayout.CENTER);
							p1.add(deletion,BorderLayout.SOUTH );
							p2.add(b,BorderLayout.CENTER);
							p2.add(insertion,BorderLayout.SOUTH);
							
							tp.removeAll();
							tp.addTab("查看全表", p1);
							tp.setEnabledAt(0, true);
							tp.addTab("添加条目", p2);
							tp.setEnabledAt(1, true);
							tp.setPreferredSize (new Dimension (700,500));
					        tp.setTabPlacement (JTabbedPane.TOP);
					        tp.setTabLayoutPolicy (JTabbedPane.SCROLL_TAB_LAYOUT);
						}
					  });
			  }
			  //退出
			  out.addActionListener(new ActionListener()
			    {
			        public void actionPerformed(ActionEvent Event) 
			        {
			      	int i=JOptionPane.showConfirmDialog(null,"是否真的需要退出系统","确认?", JOptionPane.YES_NO_CANCEL_OPTION);
			      	if(i==0)
			      	{
			      		f.dispose();
			      	}
			        }
			    }
			    );
			  //在JTextArea SQL中执行sql语句，这是将SQL面板放到程序中
			  EXSQL.addActionListener(new ActionListener()
			    {
			        public void actionPerformed(ActionEvent Event) 
			        {
			        	tp.removeAll();
			        	JPanel p1 = new JPanel();
			        	p1.setLayout(new BorderLayout());
			        	p1.add(new JScrollPane(SQL),BorderLayout.CENTER);
			        	p1.add(execute,BorderLayout.SOUTH);
						tp.addTab("执行语句", p1);
						tp.setPreferredSize (new Dimension (700,500));
				        tp.setTabPlacement (JTabbedPane.TOP);
				        tp.setTabLayoutPolicy (JTabbedPane.SCROLL_TAB_LAYOUT);
			        }
			    }
			    );
			  //一个指导面板，指导如何使用图片
			  instruction.addActionListener(new ActionListener()
			    {
			        public void actionPerformed(ActionEvent Event) 
			        {
			        	tp.removeAll();
			        	JPanel p1 = new JPanel();
			        	JTextArea text = new JTextArea();
			        	text.setEditable(false);
			        	text.setText("请将同名的图片添加到对应的文件夹下\n"
			        			+ "比如说，北京的城市图片，就放在city下，命名为{北京}，并且"
			        			+ "应该为jpg格式\n景点和导游和交通工具同理\n"
			        			+ "旅馆的命名规则为{城市+旅馆名+类型}\n"
			        			+ "文件夹如下： 城市 city 导游 guides 景点 projects"
			        			+ "\n旅馆 hotels 交通工具 media ");
			        	text.setLineWrap(true);
			        	p1.setLayout(new BorderLayout());
			        	p1.add(new JScrollPane(text),BorderLayout.CENTER);
			        	p1.add(execute,BorderLayout.SOUTH);
						tp.addTab("指导", p1);
						tp.setPreferredSize (new Dimension (700,500));
				        tp.setTabPlacement (JTabbedPane.TOP);
				        tp.setTabLayoutPolicy (JTabbedPane.SCROLL_TAB_LAYOUT);
			        }
			    }
			    );
			  
			  
			  
			  
			  f.setVisible(true);
			  f.setSize(800, 600);
			  int width=screenSize.width;
			  int height=screenSize.height;
			  int x=(width-800)/2;
			  int y=(height-600)/2;
			  f.setLocation(x,y);
		  }

		 //监听事件
		public void actionPerformed(ActionEvent e) {
			
			//执行按键按下时，在数据库里执行SQL并返回resultset，讲resultset的内容
			//转化为字符串并打印在textarea中
			if(e.getSource()==execute){
				
				GetConnection();
				String sql = SQL.getText();
				String result = new String();
				try {
					ResultSet rs = ExecuteSQL(sql);
					if(rs==null){
						return ;
					}
					int column = rs.getMetaData().getColumnCount();
					while(rs.next()){
						for(int i=1;i<=column;i++){
							String columnValue = rs.getString(i);
							result += columnValue+' ';
						}
						result+='\n';
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(f, "SQL执行失败","提示",JOptionPane.INFORMATION_MESSAGE);
				}
				SQL.removeAll();
				SQL.setText(result);
				return ;
			}
			
			//八张表对应的删除按键，从选中的表格行获取ID，并拿ID到指定表中删除这一行
			for(int i=0;i<8;i++){//
				if(e.getSource()==delete[i]){
					int row = table[i].getSelectedRow();
					if(TOOL.MakeSure()){
				
					   DefaultTableModel b = (DefaultTableModel) table[i].getModel();
					   int ID = (int) b.getValueAt(row, 0);
					   if(DeleteByID(type[i],ID)){
					     b.removeRow(row);
					     JOptionPane.showMessageDialog(f, "删除成功!","提示",JOptionPane.INFORMATION_MESSAGE);
						}
						else{
							JOptionPane.showMessageDialog(f, "操作失败，请检查约束","提示",JOptionPane.INFORMATION_MESSAGE);	
						}
					}
					return ;
				}
			}
			//八张表对应的插入，从textarea中读入字符串，处理成字符串数组交给Insert插入
			for(int i=0;i<8;i++){//
				if(e.getSource()==insert[i]){
					if(TOOL.MakeSure()){
						int length = headline[i].length;
						String set[] = new String[length];
						for(int k=0;k<length;k++){
							set[k] = textarea[i][k].getText();
						}
						if(Insert(type[i],set)){
							JOptionPane.showMessageDialog(f, "插入成功!重进界面将会更新数据","提示",JOptionPane.INFORMATION_MESSAGE);
						}
						else{
							JOptionPane.showMessageDialog(f, "操作失败，请检查约束","提示",JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			}
			return ;
		}

}
