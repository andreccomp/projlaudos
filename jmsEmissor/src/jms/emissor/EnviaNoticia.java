package jms.emissor;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;

public class EnviaNoticia {

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

			// emissor de mensagens
			MessageProducer sender = session.createProducer(topic);

			// mensagem
			TextMessage message = session.createTextMessage();
			message.setText("A copa do mundo de 2014 ser� no Brasil - " + System.currentTimeMillis());

			// enviando
			sender.send(message);

			// fechando
			sender.close();
			session.close();
			connection.close();

			System.out.println("Mensagem Enviada");
			System.exit(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
