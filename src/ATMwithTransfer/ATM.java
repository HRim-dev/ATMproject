package ATMwithTransfer;

//Tutorial 26: ATM.java
//ATM application allows users to access an account, 
//view the balance and withdraw money from the account.
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;

import javax.swing.*;
import javax.swing.event.*;

public class ATM extends JFrame
{
//GUI 컴포넌트들 선언함.
// JTextArea to display message
private JTextArea messageJTextArea1;

// JTextField to enter PIN or withdrawal amount
private JTextField numberJTextField;

// JPanel for number JButtons
private JPanel buttonsJPanel;

// JButtons for input of PIN or withdrawal amount
private JButton oneJButton;
private JButton twoJButton;
private JButton threeJButton;
private JButton fourJButton;
private JButton fiveJButton;
private JButton sixJButton;
private JButton sevenJButton;
private JButton eightJButton;
private JButton nineJButton;
private JButton zeroJButton;

// JButton to submit PIN or withdrawal amount
private JButton enterJButton;

// JButton to view balance
private JButton balanceJButton;

// JButton to withdraw from account
private JButton withdrawJButton;

// 이체하기 버튼:JButton to transfer from account
private JButton transferJButton;

// JButton to close the transaction
private JButton doneJButton;

// JPanel to get account numbers
private JPanel accountNumberJPanel;

// JLabel and JComboBox for account numbers
private JLabel accountNumberJLabel;
private JComboBox accountNumberJComboBox;

// constants for user action
private final static int ENTER_PIN = 1;
private final static int WITHDRAWAL = 2;
private final static int TRANSFER = 3;

// instance variables used to store PIN and
// firstName from database (DB로부터 읽어들인 계좌번호와 이름)
private String pin, firstName;//사용자
private String trans_pin,trans_firstName;//이체 대상

// instance variable used to distinguish user action
private int action;

// instance variables used to store user selected account number
// and PIN (사용자가 입력한 계좌번호와 PIN)
private String userAccountNumber, userPIN;
//이체 대상 계좌번호
private String transferAccountNumber;

// instance variable used to store account balance
private double balance,trans_balance;

//db객체를 위한 몇가지 변수 추가
private Connection myConnection;
private Statement myStatement;

//결과 테이블 저장
private ResultSet myResultSet;
private ResultSet myResultSet2;//이체대상의 정보를 얻어오는 결과 테이블
//GUI 컴포넌트들 선언함.
// JTextArea to display message
private JTextArea messageJTextArea;

// constructor
public ATM( String databaseDriver, String databaseURL )
{
   createUserInterface(); // set up GUI
   
} // end constructor

public ATM(String dbFileName) {
	      //db에 연결
	      try {
	         //connet to database
	    	  Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	         myConnection =DriverManager.getConnection("jdbc:ucanaccess://"+dbFileName); 
	         myStatement= myConnection.createStatement();      
	      }catch(Exception exception) {
	         exception.printStackTrace();
	      }
	      
	      //화면구성
	      createUserInterface();
}
// create and position GUI components; register event handler
private void createUserInterface()
{
   // get content pane for attaching GUI components
	  // JFrame에 GUI 컴포넌트를 붙이기 위해서는 content pane을 얻어와야 한다.
   Container contentPane = getContentPane();

   // enable explicit positioning of GUI components
   contentPane.setLayout( null );

   // set up messageJTextArea  
   messageJTextArea1 = new JTextArea();
   messageJTextArea1.setBounds( 40, 16, 288, 88 ); // (왼쪽 모서리 x, y, 폭, 높이)
   messageJTextArea1.setText(
      "Please select your account number." );
   messageJTextArea1.setBorder( 
      BorderFactory.createLoweredBevelBorder() );
   messageJTextArea1.setEditable( false ); // 편집불가능하게 함
   contentPane.add( messageJTextArea1 );

   // set up numberJTextField 
   numberJTextField = new JTextField();
   numberJTextField.setBounds( 110, 120, 128, 21 );
   numberJTextField.setBorder( 
      BorderFactory.createLoweredBevelBorder() );
   numberJTextField.setEditable( false );
   contentPane.add( numberJTextField );
   
   // set up buttonsJPanel
   buttonsJPanel = new JPanel();
   buttonsJPanel.setBounds( 44, 160, 276, 150 );
   buttonsJPanel.setBorder( BorderFactory.createEtchedBorder() );
   buttonsJPanel.setLayout( null );
   contentPane.add( buttonsJPanel );

   // set up oneJButton
   oneJButton = new JButton();
   oneJButton.setBounds( 53, 28, 24, 24 );
   oneJButton.setText( "1" );
   oneJButton.setBorder( 
      BorderFactory.createRaisedBevelBorder() );
   buttonsJPanel.add( oneJButton );
   oneJButton.addActionListener(
      // ActionListener는 인터페이스이다.
 	 // ActionListener 인터페이스에는 actionPerformed()가 선언되어 있다.
      // ActionListener 인터페이스를 구현한 익명의 클래스의 객체를 생성함
      new ActionListener() // anonymous inner class
      {
         // event handler called when oneJButton is clicked
         public void actionPerformed( ActionEvent event )
         {  // "1" 버튼이 눌려지면 아래 문장이 실행된다.
            oneJButtonActionPerformed( event ); 
         }

      } // end anonymous inner class

   ); // end call to addActionListener

   // set up twoJButton
   twoJButton = new JButton();
   twoJButton.setBounds( 77, 28, 24, 24 );
   twoJButton.setText( "2" );
   twoJButton.setBorder( 
      BorderFactory.createRaisedBevelBorder() );
   buttonsJPanel.add( twoJButton );
   twoJButton.addActionListener(

      new ActionListener() // anonymous inner class
      {
         // event handler called when twoJButton is clicked
         public void actionPerformed( ActionEvent event )
         {
            twoJButtonActionPerformed( event );
         }

      } // end anonymous inner class

   ); // end call to addActionListener

   // set up threeJButton
   threeJButton = new JButton();
   threeJButton.setBounds( 101, 28, 24, 24 );
   threeJButton.setText( "3" );
   threeJButton.setBorder( 
      BorderFactory.createRaisedBevelBorder() );
   buttonsJPanel.add( threeJButton );
   threeJButton.addActionListener(

      new ActionListener() // anonymous inner class
      {
         // event handler called when threeJButton is clicked
         public void actionPerformed( ActionEvent event )
         {
            threeJButtonActionPerformed( event );
         }

      } // end anonymous inner class

   ); // end call to addActionListener

   // set up fourJButton
   fourJButton = new JButton();
   fourJButton.setBounds( 53, 52, 24, 24 );
   fourJButton.setText( "4" );
   fourJButton.setBorder( 
      BorderFactory.createRaisedBevelBorder() );
   buttonsJPanel.add( fourJButton );
   fourJButton.addActionListener(

      new ActionListener() // anonymous inner class
      {
         // event handler called when fourJButton is clicked
         public void actionPerformed( ActionEvent event )
         {
            fourJButtonActionPerformed( event );
         }

      } // end anonymous inner class

   ); // end call to addActionListener
   
   // set up fiveJButton
   fiveJButton = new JButton();
   fiveJButton.setBounds( 77, 52, 24, 24 );
   fiveJButton.setText( "5" );
   fiveJButton.setBorder( 
      BorderFactory.createRaisedBevelBorder() );
   buttonsJPanel.add( fiveJButton );
   fiveJButton.addActionListener(

      new ActionListener() // anonymous inner class
      {
         // event handler called when fiveJButton is clicked
         public void actionPerformed( ActionEvent event )
         {
            fiveJButtonActionPerformed( event );
         }

      } // end anonymous inner class

   ); // end call to addActionListener

   // set up sixJButton
   sixJButton = new JButton();
   sixJButton.setBounds( 101, 52, 24, 24 );
   sixJButton.setText( "6" );
   sixJButton.setBorder( 
      BorderFactory.createRaisedBevelBorder() );
   buttonsJPanel.add( sixJButton );
   sixJButton.addActionListener(

      new ActionListener() // anonymous inner class
      {
         // event handler called when sixJButton is clicked
         public void actionPerformed( ActionEvent event )
         {
            sixJButtonActionPerformed( event );
         }

      } // end anonymous inner class

   ); // end call to addActionListener

   // set up sevenJButton
   sevenJButton = new JButton();
   sevenJButton.setBounds( 53, 76, 24, 24 );
   sevenJButton.setText( "7" );
   sevenJButton.setBorder( 
      BorderFactory.createRaisedBevelBorder() );
   buttonsJPanel.add( sevenJButton );
   sevenJButton.addActionListener(

      new ActionListener() // anonymous inner class
      {
         // event handler called when sevenJButton is clicked
         public void actionPerformed( ActionEvent event )
         {
            sevenJButtonActionPerformed( event );
         }

      } // end anonymous inner class

   ); // end call to addActionListener

   // set up eightJButton
   eightJButton = new JButton();
   eightJButton.setBounds( 77, 76, 24, 24 );
   eightJButton.setText( "8" );
   eightJButton.setBorder( 
      BorderFactory.createRaisedBevelBorder() );
   buttonsJPanel.add( eightJButton );
   eightJButton.addActionListener(

      new ActionListener() // anonymous inner class
      {
         // event handler called when eightJButton is clicked
         public void actionPerformed( ActionEvent event )
         {
            eightJButtonActionPerformed( event );
         }

      } // end anonymous inner class

   ); // end call to addActionListener

   // set up nineJButton
   nineJButton = new JButton();
   nineJButton.setBounds( 101, 76, 24, 24 );
   nineJButton.setText( "9" );
   nineJButton.setBorder( 
      BorderFactory.createRaisedBevelBorder() );
   buttonsJPanel.add( nineJButton );
   nineJButton.addActionListener(

      new ActionListener() // anonymous inner class
      {
         // event handler called when nineJButton is clicked
         public void actionPerformed( ActionEvent event )
         {
            nineJButtonActionPerformed( event );
         }

      } // end anonymous inner class

   ); // end call to addActionListener

   // set up zeroJButton
   zeroJButton = new JButton();
   zeroJButton.setBounds( 77, 100, 24, 24 );
   zeroJButton.setText( "0" );
   zeroJButton.setBorder(
      BorderFactory.createRaisedBevelBorder() );
   buttonsJPanel.add( zeroJButton );
   zeroJButton.addActionListener(

      new ActionListener() // anonymous inner class
      {
         // event handler called when zeroJButton is clicked
         public void actionPerformed( ActionEvent event )
         {
            zeroJButtonActionPerformed( event );
         }

      } // end anonymous inner class

   ); // end call to addActionListener

   disableKeyPad(); // disable numeric JButtons

   // set up enterJButton
   enterJButton = new JButton();
   enterJButton.setBounds( 149, 8, 72, 24 );
   enterJButton.setText( "Enter" );
   enterJButton.setBorder( 
      BorderFactory.createRaisedBevelBorder() );
   buttonsJPanel.add( enterJButton );
   enterJButton.setEnabled( false );
   enterJButton.addActionListener(

      new ActionListener() // anonymous inner class
      {
         // event handler called when enterJButton is clicked
         public void actionPerformed( ActionEvent event )
         {
            enterJButtonActionPerformed( event );
         }

      } // end anonymous inner class

   ); // end call to addActionListener

   // set up balanceJButton
   balanceJButton = new JButton();
   balanceJButton.setBounds( 149, 36, 72, 24 );
   balanceJButton.setText( "Balance" );
   balanceJButton.setBorder( 
      BorderFactory.createRaisedBevelBorder() );
   buttonsJPanel.add( balanceJButton );
   balanceJButton.setEnabled( false );
   balanceJButton.addActionListener(

      new ActionListener() // anonymous inner class
      {
         // event handler called when balanceJButton is clicked
         public void actionPerformed( ActionEvent event )
         {
            balanceJButtonActionPerformed( event );
         }

      } // end anonymous inner class

   ); // end call to addActionListener

   // set up withdrawJButton
   withdrawJButton = new JButton();
   withdrawJButton.setBounds( 149, 63, 72, 24 );
   withdrawJButton.setText( "Withdraw" );
   withdrawJButton.setBorder(
      BorderFactory.createRaisedBevelBorder() );
   withdrawJButton.setEnabled( false );
   buttonsJPanel.add( withdrawJButton );
   withdrawJButton.addActionListener(

      new ActionListener() // anonymous inner class
      {
         // event handler called when withdrawJButton is clicked
         public void actionPerformed( ActionEvent event )
         {
            withdrawJButtonActionPerformed( event );
         }

      } // end anonymous inner class

   ); // end call to addActionListener

   //이체하기 버튼 넣기
   // set up transferJButton
   transferJButton = new JButton();
   transferJButton.setBounds( 149, 88, 72, 24 );
   transferJButton.setText( "Transfer" );
   transferJButton.setBorder(
      BorderFactory.createRaisedBevelBorder() );
   transferJButton.setEnabled( false );
   buttonsJPanel.add( transferJButton );
   transferJButton.addActionListener(

      new ActionListener() // anonymous inner class
      {
         // event handler called when withdrawJButton is clicked
         public void actionPerformed( ActionEvent event )
         {
            tranferJButtonActionPerformed( event );
         }

      } // end anonymous inner class

   ); // end call to addActionListener
   
   // set up doneJButton
   doneJButton = new JButton();
   doneJButton.setBounds( 149, 115, 72, 24 );
   doneJButton.setText( "Done" );
   doneJButton.setBorder( 
      BorderFactory.createRaisedBevelBorder() );
   doneJButton.setEnabled( false );
   buttonsJPanel.add( doneJButton );
   doneJButton.addActionListener(

      new ActionListener() // anonymous inner class
      {
         // event handler called when doneJButton is clicked
         public void actionPerformed( ActionEvent event )
         {
            doneJButtonActionPerformed( event );
         }

      } // end anonymous inner class

   ); // end call to addActionListener

   // set up accountNumberJPanel
   accountNumberJPanel = new JPanel();
   accountNumberJPanel.setBounds( 44, 320, 276, 48 );
   accountNumberJPanel.setBorder( 
      BorderFactory.createEtchedBorder() );
   accountNumberJPanel.setLayout( null );
   contentPane.add( accountNumberJPanel );

   // set up accountNumberJLabel
   accountNumberJLabel = new JLabel();
   accountNumberJLabel.setBounds( 25, 15, 100, 20 );
   accountNumberJLabel.setText( "Account Number:" );
   accountNumberJPanel.add( accountNumberJLabel );

   // set up accountNumberJComboBox
   accountNumberJComboBox = new JComboBox();
   accountNumberJComboBox.setBounds( 150, 12, 96, 25 );
   accountNumberJComboBox.addItem( "" );
   accountNumberJComboBox.setSelectedIndex( 0 );
   accountNumberJPanel.add( accountNumberJComboBox );
   accountNumberJComboBox.addItemListener(

      new ItemListener() // anonymous inner class
      {
         // event handler called when account number is chosen
         public void itemStateChanged( ItemEvent event )
         {
             accountNumberJComboBoxItemStateChanged( event );
         }

      } // end anonymous inner class

   ); // end call to addItemListener

   // read account numbers from database and 
   // place them in accountNumberJComboBox
   loadAccountNumbers();

   // set properties of application's window
   setTitle( "ATM" );   // set title bar string
   setSize( 375, 410 ); // set window size
   setVisible( true );  // display window

   // ensure database connection is closed
   // when user closes application window
   addWindowListener(

      new WindowAdapter() // anonymous inner class
      {
         public void windowClosing( WindowEvent event )
         {
            frameWindowClosing( event );
         }

      } // end anonymous inner class

   ); // end addWindowListener

} // end method createUserInterface

// process oneJButton click
private void oneJButtonActionPerformed( ActionEvent event )
{
   zeroToNineJButtonActionPerformed( "1" );

} // end method oneJButtonActionPerformed

// process twoJButton click
private void twoJButtonActionPerformed( ActionEvent event )
{
   zeroToNineJButtonActionPerformed( "2" );

} // end method twoJButtonActionPerformed

// process threeJButton click
private void threeJButtonActionPerformed( ActionEvent event )
{
   zeroToNineJButtonActionPerformed( "3" );

} // end method threeJButtonActionPerformed

// process fourJButton click
private void fourJButtonActionPerformed( ActionEvent event )
{
   zeroToNineJButtonActionPerformed( "4" );

} // end method fourJButtonActionPerformed

// process fiveJButton click
private void fiveJButtonActionPerformed( ActionEvent event )
{
   zeroToNineJButtonActionPerformed( "5" );

} // end method fiveJButtonActionPerformed

// process sixJButton click
private void sixJButtonActionPerformed( ActionEvent event )
{
   zeroToNineJButtonActionPerformed( "6" );

} // end method sixJButtonActionPerformed

// process sevenJButton click
private void sevenJButtonActionPerformed( ActionEvent event )
{
   zeroToNineJButtonActionPerformed( "7" );

} // end method sevenJButtonActionPerformed

// process eightJButton click
private void eightJButtonActionPerformed( ActionEvent event )
{
   zeroToNineJButtonActionPerformed( "8" );

} // end method eightJButtonActionPerformed

// process nineJButton click
private void nineJButtonActionPerformed( ActionEvent event )
{
   zeroToNineJButtonActionPerformed( "9" );

} // end method nineJButtonActionPerformed

// process zeroJButton click
private void zeroJButtonActionPerformed( ActionEvent event )
{
   zeroToNineJButtonActionPerformed( "0" );

} // end method zeroJButtonActionPerformed

// process clicks of a numeric JButton
private void zeroToNineJButtonActionPerformed( String number )
{
   // enable enterJButton if it is disabled
   if ( !enterJButton.isEnabled() )
   {
      enterJButton.setEnabled( true ); 
   }
   
   // if user is entering PIN number display * to conceal PIN
   if ( action == ENTER_PIN )
   {
      userPIN += number; // append number to current PIN
      numberJTextField.setText( 
         numberJTextField.getText() + "*" ); // 보여줄 땐 별표로 부여줌
   }

   else // otherwise display number of JButton user clicked
   {
      numberJTextField.setText( 
         numberJTextField.getText() + number );
   }

} // end method zeroToNineJButtonsActionPerformed

// verify PIN or withdraw from account
private void enterJButtonActionPerformed( ActionEvent event )
{
  if ( action == ENTER_PIN ) // checking PIN
   {
      // get pin, first name and balance for account number
      // selected in accountNumberJComboBox
      retrieveAccountInformation(); 

      numberJTextField.setText(" "); // clear numberJTextField

      // correct PIN number 
      if ( userPIN.equals( pin ) )
      {
     	 System.out.println("로그인 성공");
         // disable enterJButton
         enterJButton.setEnabled( false ); 

         disableKeyPad(); // disable numeric JButtons

         // enable balanceJButton and withdrawJButton
         balanceJButton.setEnabled( true );
         withdrawJButton.setEnabled( true ); 
         transferJButton.setEnabled( true ); 

         // display status to user
         messageJTextArea1.setText( 
            "Welcome " + firstName + ", select a transaction." );

      } // end if part of if...else
      
      else // wrong PIN number
      {
     	System.out.println("로그인 실패");
         // indicate that incorrect PIN was provided
         messageJTextArea1.setText( 
            "Sorry, PIN number is incorrect." +
            "\nPlease re-enter the PIN number." );

         userPIN = ""; // clear user's previous PIN entry

      } // end else part of if...else

   } // end if that processes PIN

   // 출금할 액수를 입력한 후, 엔터키를 쳤다면...
   else if ( action == WITHDRAWAL ) // process withdrawal
   {
      enterJButton.setEnabled( false ); // disable enterJButton

      disableKeyPad(); // disable numeric JButtons

      // process withdrawal
      withdraw( 
         Double.parseDouble( numberJTextField.getText() ) ); // 문자열을 실수로 변환한다.

      numberJTextField.setText( "" ); // clear numberJTextField

      // enable balanceJButton and withdrawJButton
      balanceJButton.setEnabled( true );
      withdrawJButton.setEnabled( true ); 
      transferJButton.setEnabled( true ); 

   } // end if that processes withdrawal
  //이체할 금액을 입력한 후, 엔터키를 쳤다면...
   else if(action==TRANSFER)
   {
 	  enterJButton.setEnabled( false ); // disable enterJButton

       disableKeyPad(); // disable numeric JButtons
       
       transfer(Double.parseDouble( numberJTextField.getText() ) );
       numberJTextField.setText( "" ); // clear numberJTextField
   }
} // end method enterJButtonActionPerformed


// display account balance
private void balanceJButtonActionPerformed( ActionEvent event )
{
	   System.out.println("잔액 조회(Balance버튼 클릭)");
   // define display format
	  // DecimalFormat: 숫자를 일정한 형식으로 표시하고자 할 때 사용하는 클래스
   DecimalFormat dollars = new DecimalFormat( "0.00" );

   // display user's balance
   messageJTextArea1.setText( "Your current balance is $" +
      dollars.format( balance ) + "." );

} // end method balanceJButtonActionPerformed

// display withdraw action
private void withdrawJButtonActionPerformed( ActionEvent event )
{
	  System.out.println("출금(withdraw버튼 클릭)");
   // disable Balance and Withdraw and Transfer JButtons
   balanceJButton.setEnabled( false );
   withdrawJButton.setEnabled( false );
   transferJButton.setEnabled(false);

   enableKeyPad(); // enable numeric JButtons

   // display message to user
   messageJTextArea1.setText( 
      "Enter the amount you would like to withdraw" );

   // change action to indicate user will provide 
   // withdrawal amount
   action = WITHDRAWAL;

} // end method withdrawJButtonActionPerformed

//이체하기 구현
// display transfer action
	private void tranferJButtonActionPerformed(ActionEvent event) {
		  transferJButton.setEnabled(false);
	      balanceJButton.setEnabled( false );
	      withdrawJButton.setEnabled( false );
		//계좌번호 항목 리스트 활성화
		accountNumberJComboBox.setEnabled( true );
		accountNumberJComboBox.setSelectedIndex( 0 );
		
		//이체 대상 계좌번호를 선택하라는 메시지를 보여줌
	      messageJTextArea1.setText("Please select a transfer account number.");
	      
	    action=TRANSFER;
	    
	}//end method transferJButtonActionPerformed

// reset GUI
private void doneJButtonActionPerformed( ActionEvent event )
{
	  System.out.println("done버튼 클릭");
	  action=0;
   userPIN = ""; // clear userPIN

   disableKeyPad(); // disable numeric JButtons

   // disable OK, Balance, Withdraw and Done JButtons
   enterJButton.setEnabled( false );
   balanceJButton.setEnabled( false );
   withdrawJButton.setEnabled( false );
   transferJButton.setEnabled(false);
   doneJButton.setEnabled( false );

   // enable and reset accountNumberJComboBox
   accountNumberJComboBox.setEnabled( true );
   accountNumberJComboBox.setSelectedIndex( 0 );

   // reset messageJTextArea 
   messageJTextArea1.setText( 
      "Please select your account number." );

} // end method doneJButtonActionPerformed

// get account number and enable OK and Done JButtons
private void accountNumberJComboBoxItemStateChanged( 
   ItemEvent event )
{
   // get user selected account number if no transaction is
   // in process
   if ( ( event.getStateChange() == ItemEvent.SELECTED ) && // 선택 행위가 일어났고,
      ( accountNumberJComboBox.getSelectedIndex() != 0 ) ) // 무엇인가 선택했다면...
   {
       //이체하기 를 실행한다면...
       if(action==TRANSFER) {
   	    //사용자가 선택하는 이체대상 계좌번호를 trasferAccountNumber에 저장
     	  transferAccountNumber=
     			  ( String ) accountNumberJComboBox.getSelectedItem();
     	  
   	    //숫자 키패드 활성화
   	    enableKeyPad();        	  
   	    numberJTextField.setText("");
     	  
   	    retrieveAccountInformation();
   	    	System.out.println("이체대상:"+trans_firstName);
 	    messageJTextArea1.setText(
 	    		"Transfer Account Owner: " + trans_firstName	//이체 대상 계좌의 소유주이름을 보여주고 
 	    		+"\nPlease input the amount to transfer."); 	//이체금액을 입력하라는 메시지 출력
 	    accountNumberJComboBox.setEnabled(false);
       }
       else {
      // disable accountNumberJComboBox
      accountNumberJComboBox.setEnabled( false );

      // get selected account number
      userAccountNumber = 
         ( String ) accountNumberJComboBox.getSelectedItem();
      retrieveAccountInformation();
      System.out.println("사용자:"+firstName);
      // change action to indicate that user will provide
      // PIN number
      action = ENTER_PIN;
      userPIN = "";

      // prompt user to enter PIN number
      messageJTextArea1.setText( 
         "Please enter your PIN number." );

      numberJTextField.setText( "" ); // clear numberJTextField
      enableKeyPad();                 // enable numeric JButtons
      doneJButton.setEnabled( true ); // enable doneJButton
      }
   } // end if

} // end method accountNumberJComboBoxItemStateChanged

// enable numeric JButtons
private void enableKeyPad()
{
   oneJButton.setEnabled( true );   // enable oneJButton
   twoJButton.setEnabled( true );   // enable twoJButton
   threeJButton.setEnabled( true ); // enable threeJButton
   fourJButton.setEnabled( true );  // enable fourJButton
   fiveJButton.setEnabled( true );  // enable fiveJButton
   sixJButton.setEnabled( true );   // enable sixJButton
   sevenJButton.setEnabled( true ); // enable sevenJButton
   eightJButton.setEnabled( true ); // enable eightJButton
   nineJButton.setEnabled( true );  // enable nineJButton
   zeroJButton.setEnabled( true );  // enable zeroJButton

} // end method enableKeyPad

// disable numeric JButtons
private void disableKeyPad()
{
   oneJButton.setEnabled( false );   // disable oneJButton
   twoJButton.setEnabled( false );   // disable twoJButton
   threeJButton.setEnabled( false ); // disable threeJButton
   fourJButton.setEnabled( false );  // disable fourJButton
   fiveJButton.setEnabled( false );  // disable fiveJButton
   sixJButton.setEnabled( false );   // disable sixJButton
   sevenJButton.setEnabled( false ); // disable sevenJButton
   eightJButton.setEnabled( false ); // disable eightJButton
   nineJButton.setEnabled( false );  // disable nineJButton
   zeroJButton.setEnabled( false );  // disable zeroJButton

} // end method disableKeyPad

// withdraw amount from account
// 출금시, 금액 입력 후 "Enter" 버튼이 눌러질 때 호출되는 메소드
private void withdraw( double withdrawAmount )
{
   // determine if amount can be withdrawn
   if ( withdrawAmount <= balance )
   {	   System.out.println("출금성공");
      balance -= withdrawAmount; // calculate new balance

      updateBalance(); // update record in database

      // define display format
      DecimalFormat dollars = new DecimalFormat( "0.00" );

      // display balance information to user
      messageJTextArea1.setText( "The withdrawal amount is $" +
         dollars.format( withdrawAmount ) + "." );
   }
   else // amount cannot be withdrawn
   { System.out.println("출금실패");
      messageJTextArea1.setText(
         "The withdrawal amount is too large." +
         "\nSelect Withdraw and enter a different amount." );
   }

} // end method withdraw

//Transfer
//이체하기 메소드: 이체 금액을 입력 후 "Enter" 버튼이 눌러질 때 호출되는 메소드
private void transfer(double transferAmount)
{
	   retrieveAccountInformation();
	   //잔액이 충분하다면...
	   if(balance>=transferAmount) {
		   System.out.println("이체 성공");
		   //이체 실행
		   balance-=transferAmount;//사용자 잔액=사용자잔액-이체금액
		   trans_balance+=transferAmount;//이체대상 잔액=이체대상 잔액+이체금액
	         updateBalance(); // update record in database

	         // define display format
	         DecimalFormat dollars = new DecimalFormat( "0.00" );

	         // display balance information to user
	         messageJTextArea1.setText( "Success Transfer!!"
	        		 +"\n     User:"+firstName+"(balance=$"+dollars.format(balance )+")"+
	        		 "\nTransfer:"+trans_firstName+"(balance=$" + dollars.format( trans_balance )+")");

	         
	   }else{//잔액이 불충분한 경우...
		   System.out.println("이체 실패(잔액부족)");
		   messageJTextArea1.setText("The balance is insufficient."
				   +"\nPlease input the amount to transfer Again.");//이체금액을 다시 입력하라는 메시지 출력
		   
		//이체금액 다시 입력받음
		   numberJTextField.setText( "" ); // clear numberJTextField
	       enableKeyPad();                 // enable numeric JButtons		   
	   }
}
// load account numbers to accountNumberJComboBox
private void loadAccountNumbers()
{
	   //실습파일 16페이지
	   try {
		   myResultSet=myStatement.executeQuery(//쿼리문장 불러오기
				   "SELECT accountNumber From accountInformation");
		   
		   //add account numbers to accountNumberJComboBox
		   while(myResultSet.next()) {
			   accountNumberJComboBox.addItem(//콤보박스에 추가
					   myResultSet.getString("accountNumber"));
		   }
		   myResultSet.close(); //close myResultSet
		   
	   }//end try
	   catch(SQLException exception) {
		   exception.printStackTrace();
	   }
} // end method loadAccountNumbers

// get account information from database
private void retrieveAccountInformation()
{//계정정보 가져오는 메소드
	   //get account information
	   try {
		   //사용자 정보
		   myResultSet=myStatement.executeQuery(
				   "SELECT pin, "+"firstName, balanceAmount FROM accountInformation "+
				   "WHERE accountNumber= '" + userAccountNumber+"'");
		   //get next result
		   if(myResultSet.next()) {
			   pin=myResultSet.getString("pin");
			   firstName=myResultSet.getString("firstName");
			   balance=myResultSet.getDouble("balanceAmount");
		   }	
		   myResultSet.close();//close myResultSet
		   //이체대상 정보

		   
		   if(action==TRANSFER) {
			   myResultSet2=myStatement.executeQuery(
					   "SELECT pin, "+"firstName, balanceAmount FROM accountInformation "+
					   "WHERE accountNumber= '" + transferAccountNumber+"'");
			   if(myResultSet2.next()) {
				   trans_pin=myResultSet2.getString("pin");
				   trans_firstName=myResultSet2.getString("firstName");
				   trans_balance=myResultSet2.getDouble("balanceAmount");
			   }
		   	 myResultSet2.close();//close myResultSet2
		   }


	   }//end try
	   
	   catch(SQLException exception) {
		   exception.printStackTrace();
	   }
	   
} // end method retrieveAccountInformation

// update database after withdrawing
private void updateBalance()
{
	   try {
		   myStatement.executeUpdate("UPDATE accountInformation"+
				   " SET balanceAmount= "+balance+" WHERE "+
				   "accountNumber= '"+userAccountNumber+"'");
		   //이체 대상의 잔액 정보 업데이트
		   myStatement.executeUpdate("UPDATE accountInformation"+
				   " SET balanceAmount= "+trans_balance+" WHERE "+
				   "accountNumber= '"+transferAccountNumber+"'");
	   }catch(SQLException e) {
		   e.printStackTrace();
	   }
} // end method updateBalance

// close statement and database connection
private void frameWindowClosing( WindowEvent event )
{
	   try {
		   myStatement.close();
		   myConnection.close();
	   }
	   catch(SQLException sqlException) {
		   sqlException.printStackTrace();
	   }
	   finally {
		   System.exit(0);
	   }
}  // end method frameWindowClosing

// method main
public static void main( String[] args ) //데이터베이스 명령행인자를 작성해야함
{
   // check command-line arguments
   if ( args.length == 2 )//명령행인자가 2개 이면
   {
      // get command-line arguments
      String databaseDriver = args[ 0 ];
      String databaseURL = args[ 1 ];

      // create new ATM
      ATM atm = new ATM( databaseDriver, databaseURL );//ATM객체 생성
   }
   else if(args.length==1) {
 	  String dbFileName=args[0];
 	  ATM atm=new ATM(dbFileName);
   }
   else // invalid command-line arguments 그렇지않으면 오류
   {
      System.out.println( 
         "Usage: java ATM databaseDriver databaseURL" );
   }
} // end method main

} // end class ATM

/**************************************************************************
* (C) Copyright 1992-2004 by Deitel & Associates, Inc. and               *
* Pearson Education, Inc. All Rights Reserved.                           *
*                                                                        *
* DISCLAIMER: The authors and publisher of this book have used their     *
* best efforts in preparing the book. These efforts include the          *
* development, research, and testing of the theories and programs        *
* to determine their effectiveness. The authors and publisher make       *
* no warranty of any kind, expressed or implied, with regard to these    *
* programs or to the documentation contained in these books. The authors *
* and publisher shall not be liable in any event for incidental or       *
* consequential damages in connection with, or arising out of, the       *
* furnishing, performance, or use of these programs.                     *
**************************************************************************/