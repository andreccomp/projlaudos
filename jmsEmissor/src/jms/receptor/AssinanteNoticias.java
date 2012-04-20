package jms.receptor;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;

public class AssinanteNoticias {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InitialContext ic;
		try {
			ic = new InitialContext();

			// f�brica de conex�es JMS
			ConnectionFactory factory = (ConnectionFactory) ic.lookup("jms/Factory");

			// t�pico
			Topic topic = (Topic) ic.lookup("jms/noticias");

			// conex�o JMS
			Connection connection = factory.createConnection();

			// sess�o JMS
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// receptor de mensagens
			MessageConsumer receiver = session.createConsumer(topic);

			// inicializa conex�o
			connection.start();

			// recebendo
			TextMessage message = (TextMessage) receiver.receive();

			System.out.println(message.getText());

			// fechando
			receiver.close();
			session.close();
			connection.close();

			System.out.println("FIM");
			System.exit(0);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
