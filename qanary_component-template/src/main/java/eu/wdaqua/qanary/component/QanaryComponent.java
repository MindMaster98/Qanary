package eu.wdaqua.qanary.component;

//import org.apache.jena.query.Query;
//import org.apache.jena.query.QueryExecution;
//import org.apache.jena.query.QueryExecutionFactory;
//import org.apache.jena.query.QueryFactory;
//import org.apache.jena.query.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import eu.wdaqua.qanary.business.QanaryConfigurator;
import eu.wdaqua.qanary.commons.QanaryMessage;
import eu.wdaqua.qanary.commons.QanaryQuestion;
import eu.wdaqua.qanary.commons.QanaryUtils;
import eu.wdaqua.qanary.commons.triplestoreconnectors.QanaryTripleStoreConnector;
import eu.wdaqua.qanary.commons.triplestoreconnectors.QanaryTripleStoreConnectorQanaryInternal;

/**
 * represent the behavior of an annotator following the Qanary methodology
 *
 * @author AnBo
 */
@Component
public abstract class QanaryComponent {

    private static final Logger logger = LoggerFactory.getLogger(QanaryComponent.class);

    @Autowired
    private Environment env;

	private QanaryUtils utils;

	private QanaryMessage qanaryMessage;
    
    public Environment getEnvironment() {
    	return this.env;
    }
    

    /**
     * needs to be implemented for any new Qanary component
     */
    public abstract QanaryMessage process(QanaryMessage myQanaryMessage) throws Exception;

    /**
     * fetch raw data for a question
     * @throws Exception 
     */
    public String getQuestionRawData() throws Exception {
    	return this.getQanaryQuestion().getTextualRepresentation();
    }


	public void setUtils(QanaryMessage myQanaryMessage) {
		this.utils = this.getUtils(myQanaryMessage);
	}
	
	public QanaryUtils getUtils() {
		if (this.utils == null) {
			logger.error("QanaryUtils should never be null.");
			throw new java.lang.IllegalStateException("QanaryUtils should never be null.");
		}
		return(this.utils);
	}
    
    /**
     * get access to common utilities useful for the Qanary framework --> internal communication
     */
    public QanaryUtils getUtils(QanaryMessage qanaryMessage) {
    	try {
            return new QanaryUtils(qanaryMessage, new QanaryTripleStoreConnectorQanaryInternal(qanaryMessage.getEndpoint()));
		} catch (Exception e) {
			throw new RuntimeException(e); // TODO: not needed --> replace
		}
    }

    /**
     * get access to common utilities useful for the Qanary framework
     */
    public QanaryUtils getUtils(QanaryMessage qanaryMessage, QanaryTripleStoreConnector myQanaryTripleStoreConnector) {
        return new QanaryUtils(qanaryMessage, myQanaryTripleStoreConnector);
    }

    /**
     * get access to a java representation of the question for the Qanary framework --> internal communication
     */
    public QanaryQuestion<String> getQanaryQuestion(QanaryMessage qanaryMessage) {
    	try {
    		return new QanaryQuestion<String>(qanaryMessage, new QanaryTripleStoreConnectorQanaryInternal(qanaryMessage.getEndpoint()));
		} catch (Exception e) {
			throw new RuntimeException(e); // TODO: not needed --> replace
		}
    }
    
    /**
     * get current Qanary question
     */
    public QanaryQuestion<String> getQanaryQuestion(){
		if (this.getQanaryMessage() == null) {
			logger.error("QanaryMessage should never be null.");
			throw new java.lang.IllegalStateException("QanaryMessage should never be null.");
		}
    	return this.getQanaryQuestion(this.getQanaryMessage());
    }

    /**
     * get current Qanary question
     */
    public QanaryQuestion<String> getQuestion() {
        return this.getQanaryQuestion();
    }

    /**
     * get access to a java representation of the question for the Qanary framework
     */
    public QanaryQuestion<String> getQanaryQuestion(QanaryMessage qanaryMessage, QanaryConfigurator myQanaryConfigurator) {
        return new QanaryQuestion<String>(qanaryMessage, myQanaryConfigurator);
    }

	public void setQanaryMessage(QanaryMessage myQanaryMessage) {
		this.qanaryMessage = myQanaryMessage; 
	}

	public QanaryMessage getQanaryMessage() {
		return this.qanaryMessage; 
	}
}
