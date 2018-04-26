package com.codecool.shop.order.orderProcesses;
import com.codecool.shop.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an Abstract class for {@link Order} processes
 *
 * <p>The goal of this abstract class is to log the start and the finish
 * of concrete process class calls without duplicating code.</p>
 */
public abstract class AbstractProcess {
    /**
     * Logger class instance
     */
    private Logger logger = LoggerFactory.getLogger(AbstractProcess.class);
    /**
     * The process' order field on which it shall do the necessary changes
     */
    protected Order order;

    /**
     * Abstract constructor for processes
     * @param order The order field gets initialized through the constructor
     */
    public AbstractProcess(Order order){
        this.order = order;
    }

    /**
     * This abstract method helps logging with returning the name of the process
     * @return Returns an AbstractProcess object if called through an extended concrete class
     */
    public abstract ProcessName getName();

    /**
     * Calling this will make the logging and calls the process itself
     */
    public void doTheSteps(){
        stepBefore();
        process();
        stepAfter();
    }

    /**
     * Logs before the process
     */
    private void stepBefore(){
        logger.info("User {} starts {} process.", order.getUserId(), this.getName());
    }

    /**
     * Abstract process method which becomes concrete in the process classes
     */
    public abstract void process();

    /**
     * Logs after the process is finished
     */
    private void stepAfter(){
        logger.info("User {} finished {} process.", order.getUserId(), this.getName());
    }
}
