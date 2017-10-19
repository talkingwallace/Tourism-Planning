package finalassignment;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

class TOOL{
	
	static Font standard = new Font("方正兰亭超细黑简体", Font.BOLD, 20);
	
	static void ReportError(Exception e){
		System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	}
	
	static boolean MakeSure(){
		
		boolean value = false;
		int i=JOptionPane.showConfirmDialog(null, "确认吗？","提示:"
				+ "", JOptionPane.YES_NO_OPTION);
		if(i==JOptionPane.OK_OPTION){
			value = true;
		}
		return value;
	}
}


class PicPanel extends JPanel implements ActionListener{
	
	String Path = "./scenery/";
	String filelist[];
	File file = new File(Path);
	private int count = 0;
	JButton left,right;
	JLabel picarea;
	Icon icon = new ImageIcon();
	
	PicPanel(){
		
		filelist = file.list();
		this.setBounds(14, 13, 1254, 617);
		this.setLayout(null);
		icon = new ImageIcon(Path+filelist[0]);
		picarea = new JLabel(icon);
		picarea.setBounds(54, 13, 1140, 591);
		left = new JButton("<-");
		left.setBounds(14, 230, 60, 91);
		right = new JButton("->");
		right.setBounds(1150, 245, 60, 91);
		this.add(picarea);
		this.add(left);
		this.add(right);
		left.addActionListener(this);
		right.addActionListener(this);
		this.setBackground(new Color(230,239,182));
	}

	public void actionPerformed(ActionEvent e) {

		if(e.getSource()==left){
			count--;
			if(count<0){
				count = filelist.length-1;
			}
		}
		else if(e.getSource()==right){
			count++;
			if(count>=filelist.length){
				count = 0;
			}	
		}
		if(filelist[count].indexOf(".jpg")>0){
			icon = new ImageIcon(Path+filelist[count]);
			picarea.setIcon(icon);
		}
		this.repaint();
	}
}

class DispPanel extends JPanel{
	
	protected JLabel pic;
	protected JTextField headline;
	protected JTextArea textArea;
	protected JButton handle;
	
	public void Setpic(Icon pic){
		this.pic.setIcon(pic);
	}
	
	public void Setheadline(String value){
		headline.setText(value);
	}
	
	public void SettextArea(String value){
		textArea.setText(value);
	}
	
	DispPanel(){
		
		this.setBounds(15, 15, 800, 450);
		this.setBackground(new Color(230,239,182));
		this.setLayout(null);
		pic = new JLabel();
		pic.setBounds(410, 0, 430, 500);
		this.add(pic);
		headline = new JTextField();
		headline.setEditable(false);
		headline.setBounds(14, 13, 387, 52);
		headline.setBackground(new Color(206,214,173));
		this.add(headline);
		headline.setColumns(10);
		headline.setFont(TOOL.standard);
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(TOOL.standard);
		textArea.setBackground(new Color(206,214,173));
		textArea.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 76, 387, 301);
		this.add(scrollPane);
		
		scrollPane.setViewportView(textArea);
	
		JTextField textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setText("概况");
		scrollPane.setColumnHeaderView(textField_1);
		textField_1.setColumns(10);
	}
	
}

class GuidesPanel extends DispPanel implements ActionListener{
	
	private String ID,name,speci,gender,city;
	int price;
	String Path =  "./guides/";
	public void showmydata(){
		System.out.println(ID+name+speci+price+gender+city);
	}
	public String showmyname(){
		return name;
	}
	GuidesPanel(String a,String b,String c,int d,String e,String f){
		ID = a;name = b;speci = c;price = d;gender = e;city =f;
		this.headline.setText(name);
		this.textArea.setText("注册ID:"+ID+"\n"+"性别:"+gender+
				"\n费用:"+price+"\n个人介绍:\n"+speci);
		handle = new JButton("雇用");
		handle.setBounds(14, 390, 387, 47);
		handle.addActionListener(this);
		this.add(handle);
		this.setBackground(new Color(230,239,182));
		Icon picture = new ImageIcon(Path+name+".jpg");
		this.Setpic(picture);
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource()==handle){
			Object a[]={"雇用位于"+city+"的导游"+name,price};
			Main.model.addRow(a);
		}
	}
}

class ProjectsPanel extends DispPanel implements ActionListener{
	
	private String city,name,speci,start,end;
	private int price;
	protected String Path = "./projects/";
	
	public void showmydata(){
		System.out.println(city+name+speci+start+end+price);
	}
	
	public String showmyname(){
		return name;
	}
	
	ProjectsPanel(String a,String b,String c,String d,String f,int e){
		
		super();
		city = a;name = b;speci = c;start = d;end = f;price = e;
		pic.setIcon(new ImageIcon(this.Path+name));
		this.headline.setText("景点名称:"+name);
		this.textArea.setText("所在城市:"+city+"\n\n"
				+ "开放时间:"+start+"  "+"结束时间:"+end+"\n\n"+"门票价格:"+price+"\n\n"+"景点介绍:\n"+speci);
		handle = new JButton("加入计划");
		handle.setBounds(14, 390, 387, 47);
		handle.addActionListener(this);
		this.add(handle);
		this.setBackground(new Color(230,239,182));
		Icon picture = new ImageIcon(Path+name+".jpg");
		this.Setpic(picture);
		
	}

	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==handle){
			Object a[]={"前往"+name+"游玩",price};
			Main.model.addRow(a);
		}
	}
}

class HotelsPanel extends DispPanel implements ActionListener{
	
	String name,city,location,type,contact;
	int price;
	protected String Path = "./hotels/";
	
	public void showmydata(){
		System.out.println(name+city+location+type+contact+price);
	}
	
	public String showmyname(){
		return name+type;
	}
	
	HotelsPanel(String a,String b,String c,String d,String f,int e){
		
		super();
		name = a;city = b;location =c;type = d;price = e;contact = f;
		this.headline.setText(name);
		this.textArea.setText("所在城市:"+city+"\n"
				+ "房间类型:"+type+"\n"
						+ "价格:"+price+" 联系方式:"+contact+"\n地址:"+location);
		handle = new JButton("入住");
		handle.setBounds(14, 390, 387, 47);
		handle.addActionListener(this);
		this.add(handle);
		this.setBackground(new Color(230,239,182));
		Icon picture = new ImageIcon(Path+city+name+type+".jpg");
		this.Setpic(picture);
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==handle){
			Object a[]={"计划入住"+name+"旅店"+type,price};
			Main.model.addRow(a);
		}
	}	
}

class TransportPanel extends DispPanel implements ActionListener{
	
	String from,to,start,end,media;
	int cost;
	protected String Path = "./media/";
	
	public void showmydata(){
		System.out.println(from+to+start+end+media+cost);
	}
	
	public String showmyname(){
		return "乘"+media+"去往"+to;
	}
	
	TransportPanel(String a,String b,String c,String d,String e,int cost){
		from = a;to = b;start = d;end = e;media = c;this.cost = cost;
		
		this.pic.setIcon(new ImageIcon(Path+media+".jpg"));
		this.headline.setText(from+"至"+to);
		this.textArea.setText("出发:"+start+'\n'+"到达:"+end+"\n交通工具:"+media+"\n花费"+cost);
		handle = new JButton("加入行程");
		handle.setBounds(14, 390, 387, 47);
		handle.addActionListener(this);
		this.add(handle);
		this.setBackground(new Color(230,239,182));
	}

	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==handle){
			Object a[]={"乘坐"+media+"从"+from+"前往"+to,cost};
			Main.model.addRow(a);
		}
	}
}




//打包板块，包含了一类的所有信息，并且与查询模块一起整合


class PackedPanel <T extends DispPanel> extends JPanel implements ActionListener{
	
	Map<String,T> data = new TreeMap();
	JTabbedPane tp = new JTabbedPane();
	JPanel search = new JPanel();
	String TreeInfo[];
	
	DefaultMutableTreeNode tableroot = new DefaultMutableTreeNode("索引");
	JTree tree = new JTree(tableroot);
	JButton jump = new JButton("跳往");
	
	public void addmap(String a,T t){
		data.put(a, t);
	}
	
	public void AddTree(String name,ArrayList<String> index){
		
		DefaultMutableTreeNode root=new DefaultMutableTreeNode(name);
		DefaultMutableTreeNode node[] = new DefaultMutableTreeNode[index.size()];
		for(int i=0;i<index.size();i++){
			node[i] =new DefaultMutableTreeNode(index.get(i));
			root.add(node[i]);
		}
		for(String key:data.keySet()){
			for(int i=0;i<index.size();i++){
				if(key.indexOf(index.get(i))>0){
					node[i].add(new DefaultMutableTreeNode(key));
				}
			}
		}
		tableroot.add(root);
	}
	
	public void CreateSearch(){
		
		search.setLayout(null);
		tree.setBackground(new Color(230,239,182));
		JScrollPane	sp = new JScrollPane(tree);
		sp.setBounds(14, 13, 600, 229);
		jump.addActionListener(this);
		search.add(sp);
		jump.setBounds(14,260,80,60);
		search.add(jump);
		search.setBackground(new Color(230,239,182));
	}
	
	public void DisableSearch(){
		tp.remove(0);
	}
	
	
	public void Paint(){
		
		tp.addTab("快速检索", search);
		for(String key:data.keySet()){
			tp.add(key, data.get(key));
		}
	}
	
	PackedPanel(){
		
		tp.setBounds(0, 0, 845, 540);
		tp.setFont(TOOL.standard);
		tp.setTabLayoutPolicy (JTabbedPane.SCROLL_TAB_LAYOUT);
		tp.setBackground(new Color(230,239,182));
		this.setLayout(null);this.setBounds(15, 15, 815, 500);
		this.setBackground(new Color(230,239,182));
		this.add(tp);

	}

	public void actionPerformed(ActionEvent e) {
	
		if(e.getSource()==jump){
			try{
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				String name = node.toString();
				tp.setSelectedComponent(data.get(name));
			}catch(Exception f){
				JOptionPane.showMessageDialog(this, "抱歉,没有这个面板哦","提示",JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

}





class CityPanel extends JPanel{
	
	private String name;
	private String speci;
	private String sql1,sql2,sql3,sql4;
	private String Path = "./city/";
	private DispPanel dispcity = new DispPanel();
	JTabbedPane tp = new JTabbedPane();
	PackedPanel<HotelsPanel> hotels = new PackedPanel<HotelsPanel>();
	PackedPanel<GuidesPanel> guides = new PackedPanel<GuidesPanel>();
	PackedPanel<ProjectsPanel> pro = new PackedPanel<ProjectsPanel>();
	PackedPanel<TransportPanel> transport = new PackedPanel<TransportPanel>();
	
	public String showmyname(){
		return name;
	}
	
	private void GetProjects(){
		try{
			ResultSet rs = Manager.ExecuteSQL(sql1);
			for(int num=1;rs.next();num++){
				String result[] = new String[5];
				for(int i=2;i<=6;i++){
					result[i-2]=rs.getString(i);
				}
				int price = rs.getInt(7);
				ProjectsPanel a = new ProjectsPanel(result[0],result[1],result[2],result[3],result[4],price);
				pro.addmap(num+":"+a.showmyname(), a);
			}
			pro.Paint();
		}catch(Exception e){
			TOOL.ReportError(e);
		}
	}
	
	private void GetGuides(){
		try{
			ResultSet rs = Manager.ExecuteSQL(sql2);
			for(int num=1;rs.next();num++){
				String result[] = new String[5];
				int price;
				for(int i=1;i<=3;i++){
					result[i-1]=rs.getString(i);
				}
				price = rs.getInt(4);
				result[3] = rs.getString(5);
				result[4] = rs.getString(6);
				GuidesPanel a = new GuidesPanel(result[0],result[1],result[2],price,result[3],result[4]);
				guides.addmap(num+":"+a.showmyname(), a);
			}
			guides.Paint();
		}catch(Exception e){
			TOOL.ReportError(e);
		}
	}
	
	private void GetHotels(){
		try{
			ResultSet rs = Manager.ExecuteSQL(sql4);
			for(int num=1;rs.next();num++){
				int price;
				String result[] = new String[5];
				for(int i=2;i<=5;i++){
					result[i-2]=rs.getString(i);
				}
				price = rs.getInt(6);
				result[4] = rs.getString(7);
				HotelsPanel a = new HotelsPanel(result[0],result[1],result[2],result[3],result[4],price);
				hotels.addmap(num+":"+a.showmyname(), a);
				}
			hotels.Paint();
		}catch(Exception e){
			TOOL.ReportError(e);
		}
	}
	
	private void GetTransport(){
		try{
			ResultSet rs = Manager.ExecuteSQL(sql3);
			for(int num=1;rs.next();num++){
				int price;
				String result[] = new String[6];
				for(int i=2;i<=6;i++){
					result[i-2]=rs.getString(i);
				}
				price = rs.getInt(7);
				TransportPanel a = new TransportPanel(result[0],result[1],result[2],result[3],result[4],price);
				transport.addmap(num+":"+a.showmyname(), a);
			}
			transport.Paint();
		}catch(Exception e){
			TOOL.ReportError(e);
		}
	}
	
	private ArrayList<String> GetIndex(String sql){
		
		ResultSet rs = Manager.ExecuteSQL(sql);
		ArrayList<String> index = new ArrayList<String>();
		try {
			while(rs.next()){
				index.add(rs.getString(1));
			}
		} catch (SQLException e) {
			TOOL.ReportError(e);
		}
		return index;
	}
	
	private void CreateSubSearchPanel(){
		
		String sql[]={
				"SELECT DISTINCT NAME FROM HOTELS WHERE CITY="+"'"+name+"';",
				"SELECT DISTINCT TYPE FROM HOTELS WHERE CITY="+"'"+name+"';",
				"SELECT DISTINCT END FROM TRANSPORT WHERE START="+"'"+name+"';",
				"SELECT DISTINCT VEHICLES FROM TRANSPORT WHERE START="+"'"+name+"';"
		};
		String nodename[] ={
			"旅馆名称","种类","目的地","交通工具"	
		};
		ArrayList<String> a = GetIndex(sql[0]);
		ArrayList<String> b = GetIndex(sql[1]);
		ArrayList<String> c = GetIndex(sql[2]);
		ArrayList<String> d = GetIndex(sql[3]);
		hotels.AddTree(nodename[0], a);
		hotels.AddTree(nodename[1], b);
		transport.AddTree(nodename[2], c);
		transport.AddTree(nodename[3], d);
		hotels.CreateSearch();
		transport.CreateSearch();
		pro.DisableSearch();
		guides.DisableSearch();
		
	}
	
	CityPanel(String n,String s){
		
		name = n;
		speci = s;
		sql1 = "SELECT * FROM PROJECTS WHERE CITY ="+"'"+name+"';";
		sql2 = "SELECT * FROM  GUIDES WHERE CITY ="+"'"+name+"';";
		sql3 = "SELECT * FROM TRANSPORT WHERE START ="+"'"+name+"'ORDER BY END;";
		sql4 = "SELECT * FROM HOTELS WHERE CITY="+"'"+name+"' ORDER BY NAME;";
		GetProjects();
		GetGuides();
		GetHotels();
		GetTransport();
		CreateSubSearchPanel();
		this.setBounds(15, 15, 830, 530);
		this.setBackground(new Color(230,239,182));
		this.setLayout(null);
		tp.setBounds(15, 15, 845, 530);
		tp.setFont(TOOL.standard);
		tp.setTabLayoutPolicy (JTabbedPane.SCROLL_TAB_LAYOUT);
		tp.setBackground(new Color(230,239,182));
		this.add(tp);
		dispcity.Setheadline(name);
		dispcity.SettextArea("城市简介:\n"+speci);
		Icon picture = new ImageIcon(Path+name+".jpg");
		dispcity.Setpic(picture);
		tp.add("城市简介",dispcity);
		tp.addTab("景点一览", pro);
		tp.addTab("住宿信息", hotels);
		tp.addTab("当地导游", guides);
		tp.addTab("交通信息", transport);
		
	}
}




public class Main {
	
	//-----------------------------------初始化数据-------------------
	static String findcity = "select * from ALLCITY;";
	private String currentusert;
	private String userpassword;

	static JTable table;
	static DefaultTableModel model;
	//因为需要其他版块传入数据给这个表，所以定义为Main的静态以方便调用; 
	static ArrayList<CityPanel> CityList = new ArrayList();
	static String menu[]={
			"随便看看风景","制定行程","我的行程", "修改密码", "管理员入口", "退出","主界面"
	};
	static JMenuItem [] item= new JMenuItem[7];
	private static char[] administorPasswords = {'w','o','s','h','i','g','u','a','n','l'
			,'i','y','u','a','n'};
	//-----------------------------调整窗口位置用----------------
	Toolkit kit=Toolkit.getDefaultToolkit();
    Dimension screenSize=kit.getScreenSize();
	//-------------------------------主面板与窗口----------------------
	JFrame frame;
	JPanel Current;

//-------------------------------------------------------------------类方法---------------

	public void GetCity(){
		
		try {
			ResultSet rs = Manager.ExecuteSQL(findcity);
			while(rs.next()){
				String a = rs.getString(2),b = rs.getString(3);
				CityList.add(new CityPanel(a,b));
			}
		} catch (SQLException e) {
			TOOL.ReportError(e);
		}

	}
	
	private void changeto(Component a){
		Current.removeAll();
		Current.add(a);
		Current.repaint();
	}
	private void changeto(Component a[]){
		Current.removeAll();
		for(int i=0;i<a.length;i++){
			Current.add(a[i]);
		}
		Current.repaint();
	}
	
//------------------------------------------------------------------构造函数---------------	
	
	Main(String currentuser,String Password){
		
		this.currentusert = currentuser;
		this.userpassword = Password;
		frame = new JFrame("欢迎您！"+currentuser);
		Current = new JPanel();
		GetCity();
//----------------------------------------------------------------用户版块----------------------
		
		
		JPanel myroute = new JPanel();
		myroute.setBounds(14, 13, 1254, 617);
		myroute.setBackground(new Color(230,239,182));
		myroute.setLayout(null);
		
//----------------------------------------------------------------管理员密码板块--------------	
		final JPanel passwordPanel = new JPanel();
		JLabel notice= new JLabel("请输入管理员密码");
		notice.setFont(TOOL.standard);
		notice.setBounds(15,15,320,100);
		passwordPanel.setBackground(new Color(230,239,182));
		passwordPanel.setBounds(14, 24, 1165, 606);
		passwordPanel.setLayout(null);
		final JPasswordField password = new JPasswordField();
		password.setBounds(200, 150, 538, 37);
		passwordPanel.add(password);
		JButton admit = new JButton("确认");
		admit.setBounds(300, 300, 300, 50);
		admit.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				char[] get = password.getPassword();

				if(Arrays.equals(get, administorPasswords)){
					new Manager().CreateInterface();
				}
				else{
					JOptionPane.showMessageDialog(frame, "您的密码错误","提示",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		passwordPanel.add(admit);
		passwordPanel.add(notice);
//----------------------------------------------------------图片浏览板块-----------------	
		
		PicPanel picpanel = new PicPanel();

//----------------------------------------------------------工具栏----------------	
		for(int i=0;i<7;i++){
			item[i] = new JMenuItem(menu[i]);
			item[i].setFont(TOOL.standard);
		}
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		JMenu mainfunc = new JMenu("主要功能");
		mainfunc.setFont(new Font("方正兰亭超细黑简体", Font.BOLD, 20));
		menuBar.add(mainfunc);
		JMenu mydata = new JMenu("我的信息");
		mydata.setFont(new Font("方正兰亭超细黑简体", Font.BOLD, 20));
		menuBar.add(mydata);
		JMenu other = new JMenu("其他");
		other.setFont(new Font("方正兰亭超细黑简体", Font.BOLD, 20));
		menuBar.add(other);
		mainfunc.add(item[6]);mainfunc.add(item[0]);mainfunc.add(item[1]);
		mydata.add(item[2]);mydata.add(item[3]);
		other.add(item[4]);other.add(item[5]);
		
//----------------------------------------------------------行程版块------------------		
		
		JTabbedPane makeRoute = new JTabbedPane(JTabbedPane.TOP);
		JPanel ButtonPanel = new JPanel(new FlowLayout());
		JButton save =new JButton("结算保存") ,time = new JButton("添加日期"),
				delete = new JButton("删除"),add = new JButton("添加空行程");
		ButtonPanel.add(save);ButtonPanel.add(delete);
		ButtonPanel.add(add);ButtonPanel.add(time);
		makeRoute.setBounds(14, 32, 870, 600);
		makeRoute.setFont(TOOL.standard);
		for(CityPanel key:CityList){
			makeRoute.addTab(key.showmyname(), key);
		}
		JPanel tablePanel = new JPanel();
		tablePanel.setBounds(891, 15, 356, 617);
		tablePanel.setLayout(new BorderLayout());
		 Object[][] Info={};//创建表格中的空数据
		String[] head={"行程","开销"};//创建表格中的横标题
		model = new DefaultTableModel(Info,head);
		table = new JTable(model);
		JScrollPane f = new JScrollPane(table);
		table.getColumnModel().getColumn(0).setPreferredWidth(400);
		tablePanel.add(f,BorderLayout.CENTER);
		tablePanel.add(table.getTableHeader(),BorderLayout.NORTH);
		tablePanel.add(ButtonPanel,BorderLayout.SOUTH);
		
		makeRoute.setBackground(new Color(230,239,182));
		ButtonPanel.setBackground(new Color(230,239,182));
		tablePanel.setBackground(new Color(230,239,182));
		table.setBackground(new Color(206,214,173));
		table.setGridColor(new Color(230,239,182));
		table.setRowHeight(50);
		table.setFont(TOOL.standard);
//----------------------------------------------------------------主界面----------------
		final JPanel MainPanel = new JPanel();
		MainPanel.setLayout(null);
		MainPanel.setBounds(0, 0, 1193, 643);
		Icon b1 = new ImageIcon("./route.jpg");
		Icon b2 = new ImageIcon("./see.jpg");
		Icon b3 = new ImageIcon("./reserve.jpg");
		JButton makeroute = new JButton("New button");
		makeroute.setBounds(93, 222, 250, 250);
		makeroute.setIcon(b1);
		MainPanel.add(makeroute);
		
		JButton seearound = new JButton("New button");
		seearound.setBounds(454, 222, 250, 250);
		seearound.setIcon(b2);
		MainPanel.add(seearound);
		
		JButton seemyroute = new JButton("New button");
		seemyroute.setBounds(831, 222, 250, 250);
		seemyroute.setIcon(b3);
		MainPanel.add(seemyroute);		
		
		MainPanel.setBackground(new Color(166,190,66));

//---------------------------------------------------事件处理---------------------
		
		
		makeroute.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				Component a[] = {makeRoute,tablePanel};
				changeto(a);
			}
		});
		
		seearound.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				changeto(picpanel);
			}
		});
		
		seemyroute.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				
				myroute.removeAll();
				String sql = "SELECT * FROM ROUTE WHERE USER ="+"'"+currentuser+"';";
				ResultSet result = Manager.ExecuteSQL(sql);
				String headline[] = {"编号","用户","文件名称","内容"};
				JTable table = Manager.CreateTable(result,headline);
				JLabel header = new JLabel("行程内容");
				JTextArea text = new JTextArea();
				JButton show = new JButton("显示完整内容");
				JLabel title = new JLabel("我的行程");
				JButton deleteroute = new JButton("删除选定");
				
				text.setFont(TOOL.standard);
				table.setFont(TOOL.standard);
				header.setFont(TOOL.standard);
				title.setFont(new Font("方正兰亭超细黑简体", Font.BOLD, 30));
				
				JScrollPane sp1 = new JScrollPane(table);
				JScrollPane sp2 = new JScrollPane(text);
				
				deleteroute.setBounds(530, 497, 112, 27);
				sp1.setBounds(14, 87, 502, 517);
				sp2.setBounds(658, 87, 484, 517);
				show.setBounds(532, 577, 112, 27);
				title.setBounds(14, 13, 502, 61);
				sp2.setColumnHeaderView(header);
				text.setBackground(new Color(230,239,182));
				myroute.add(sp1);
				myroute.add(sp2);
				myroute.add(title);
				myroute.add(show);
				myroute.add(deleteroute);
				myroute.repaint();
				changeto(myroute);
				
				show.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent arg0) {
						try{
							int row = table.getSelectedRow();
							text.setText((String) table.getValueAt(row, 3));
						}catch(Exception e){
							
						}
					}
				});	
				
				deleteroute.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent arg0) {
						try{
							int k=JOptionPane.showConfirmDialog(null,"确定删除吗？？","确认?", JOptionPane.YES_NO_CANCEL_OPTION);
							if(k==0){
								int row = table.getSelectedRow();
								int ID = (Integer)table.getValueAt(row, 0);
								Manager.DeleteByID("ROUTE", ID);
								DefaultTableModel b = (DefaultTableModel) table.getModel();
								b.removeRow(row);
							}
						}catch(Exception e){
						}
					}
				});	
			}
		});
		
		
		item[0].addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				changeto(picpanel);
			}
		});
		
		item[1].addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				Component a[] = {makeRoute,tablePanel};
				changeto(a);
			}
		});
		
		item[2].addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				changeto(myroute);
			}
		});
		
		item[3].addActionListener(new ActionListener(){


			public void actionPerformed(ActionEvent arg0) {
				
				JFrame handleframe = new JFrame("输入新密码");
				JPanel handle = new JPanel(new BorderLayout());
				JTextField handlearea = new JTextField();
				JButton handlebutton = new JButton("提交");
				JLabel notice = new JLabel("请输入一个新密码");
				int width=screenSize.width;
				int height=screenSize.height;
				int x=(width-800)/2;
				int y=(height-600)/2;
				notice.setFont(TOOL.standard);
				handleframe.setLocation(x,y);
				handlearea.setFont(TOOL.standard);
				handleframe.setSize(300, 200);
				handleframe.add(handle);
				handle.add(handlearea,BorderLayout.CENTER);
				handle.add(handlebutton,BorderLayout.SOUTH);
				handle.add(notice,BorderLayout.NORTH);
				handleframe.setVisible(true);
				
				handlebutton.addActionListener(new ActionListener(){
					
					public void actionPerformed(ActionEvent arg0) {
						
						String newpassword = handlearea.getText();
						int k=JOptionPane.showConfirmDialog(null,"确定新密码吗?","确认?", JOptionPane.YES_NO_CANCEL_OPTION);
						if(k==0){
							newpassword = handlearea.getText();
							String sql = "update USERS SET PASSWORD = '"+newpassword+"' where USERNAME= '"+currentuser+"';";
							Manager.ExecuteSQL(sql);
							JOptionPane.showMessageDialog(frame, "保存成功！","提示",JOptionPane.INFORMATION_MESSAGE);
							handleframe.dispose();
						}
					}
				});
			}
		});
		
		item[4].addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				changeto(passwordPanel);
			}
		});
		
		item[5].addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				int k=JOptionPane.showConfirmDialog(null,"确定退出吗？？","确认?", JOptionPane.YES_NO_CANCEL_OPTION);
				if(k==0){
					frame.dispose();
				}
			}
		});
		
		item[6].addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				changeto(MainPanel);
			}
		});
		
		add.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				String blank[]={"",""};
				model.addRow(blank);
			}
		});
		
		time.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				String value[]={"年 月 日",""};
				model.addRow(value);
			}
		});
		
		delete.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				try{
					int row = table.getSelectedRow();
					model.removeRow(row);
				}catch(Exception e){}
			}
		});
		
		//----------------------------------------------------------用户提交行程-------
		save.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
			
			//---------------------------------------------------------------
				JFrame handleframe = new JFrame("提交");
				JPanel handle = new JPanel(new BorderLayout());
				JTextField handlearea = new JTextField();
				JButton handlebutton = new JButton("提交");
				JLabel notice = new JLabel("请为行程取一个名字");
				int width=screenSize.width;
				int height=screenSize.height;
				int x=(width-800)/2;
				int y=(height-600)/2;
				notice.setFont(TOOL.standard);
				handleframe.setLocation(x,y);
				handlearea.setFont(TOOL.standard);
				handleframe.setSize(300, 200);
				handleframe.add(handle);
				handle.add(handlearea,BorderLayout.CENTER);
				handle.add(handlebutton,BorderLayout.SOUTH);
				handle.add(notice,BorderLayout.NORTH);
				handleframe.setVisible(true);

			//----------------------------------------------处理提交------------------	
				handlebutton.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent arg0) {
						String filename = handlearea.getText();
						String ID = filename.hashCode()+"";
						String route = new String();
						int i = model.getRowCount();
						int cost = 0;
						for(int k=0;k<i;k++){
							try{
								route += model.getValueAt(k, 0);
								cost += (int)model.getValueAt(k, 1);
								route+="\n";
							}catch(Exception e){
							}
						}
						String set[]={
							ID,currentuser,filename,route+"\n总费用"+cost
						};
				      	int k=JOptionPane.showConfirmDialog(null,"行程:"+filename+"将要花费"+cost
				      			+" 确认保留吗？","确认?", JOptionPane.YES_NO_CANCEL_OPTION);
				      	if(k==0){
				      		if(Manager.Insert("ROUTE", set)){
				      			JOptionPane.showMessageDialog(frame, "保存成功！","提示",JOptionPane.INFORMATION_MESSAGE);
				      			handleframe.dispose();
				      		}
				      		else{
				      			JOptionPane.showMessageDialog(frame, "保存失败，请不要重名哦！","提示",JOptionPane.INFORMATION_MESSAGE);
				      		}
				      	}
					}
				});
			}
		});
		
		Current.setBounds(0, 0, 1300, 760);
		Current.setLayout(null);
		Current.add(MainPanel);
		Current.setBackground(new Color(166,190,66));
		//------------------------最后的frame初始化-------------
		int width=screenSize.width;
		int height=screenSize.height;
		int x=(width-1000)/2;
		int y=(height-800)/2;
		frame.add(Current);
		frame.setLocation(x, y);
		frame.setBounds(100, 100, 1300, 760);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setBackground(new Color(166,190,66));
		frame.setVisible(true);
	}
}
