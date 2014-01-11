package org.shredzone.flattr4j.requests.base;

import org.shredzone.flattr4j.OpenService;
import org.shredzone.flattr4j.exception.FlattrException;

import com.octo.android.robospice.request.SpiceRequest;

/**
 * The basic flattr request providing a template to derive all other requests from
 * 
 * @author tuxbox
 *
 */
public abstract class BaseFlattrRequest<RESULT, ServiceType extends OpenService> extends SpiceRequest<RESULT> {

  /**
   * Instantiates the request
   * 
   * @param clazz
   *  the class that defines the result type of the flattr request
   */
  protected BaseFlattrRequest(Class<RESULT> clazz) {
		super(clazz);
	}

  /**
   * this method actually executes the request using the given service that is a implementation of or extends {@link OpenService}
   * 
   * @param service
   *  the service to be used to fulfill the request
   * @return the defined response
   * @throws FlattrException
   */
  protected abstract RESULT handleRequest(ServiceType service) throws FlattrException;
	
  /**
   * this hook method is executed prior to the execution of the request. It allows request specific configurations to be applied to the request.
   * When you override this method you should always call super.{@link #configureService(ServiceType)}
   * 
   * @param service
   *  the service to be used to fulfill the request
   * @throws FlattrException
   */
  protected void configureService(ServiceType service) throws FlattrException {
  }
  
  /**
   * hook method to create the appropriate service instance
   * 
   * @return the service instance to fulfill this request
   */
  protected abstract ServiceType createService();
  
  @Override
  public RESULT loadDataFromNetwork() throws Exception {
    ServiceType service = createService();
    configureService(service);
    return handleRequest(service);
  }

}
