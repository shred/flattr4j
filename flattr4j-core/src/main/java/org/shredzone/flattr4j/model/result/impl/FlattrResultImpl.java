package org.shredzone.flattr4j.model.result.impl;

import org.shredzone.flattr4j.connector.FlattrObject;
import org.shredzone.flattr4j.model.Resource;
import org.shredzone.flattr4j.model.Thing;
import org.shredzone.flattr4j.model.result.FlattrResult;
import org.shredzone.flattr4j.model.result.ThingResult;
import org.shredzone.flattr4j.model.result.ThingUpdateResult;

/**
 * 
 * @author iulius
 *
 */
public class FlattrResultImpl extends Resource implements FlattrResult, ThingResult, ThingUpdateResult {

  /**
   * 
   */
  private static final long serialVersionUID = 1206688409906902301L;

  /**
   * 
   * @author iulius
   *
   */
  public enum Message {
    OK("ok"),
    STARTED("started"),
    PAUSED("paused"),
    FOUND("found"),
    NOT_FOUND("not_found");
    
    private String message;
    
    private Message(String message) {
      this.message = message;
    }
    
    /**
     * 
     * @return
     */
    public String message() {
      return message;
    }
    
    /**
     * 
     * @param message
     * @return
     */
    public static Message forMessage(String message) {
      Message result = null;
      for( Message msg : values() ) {
        if( msg.message.equals(message) ) {
          result = msg;
          break;
        }
      }
      return result;
    }
  }
  
  private transient Message message;
  private transient String description;
  private transient String location;
  private transient Thing thing;
  private transient String id;
  
  public FlattrResultImpl(FlattrObject data) {
    super(data);
  }

  /*
   * (non-Javadoc)
   * @see org.shredzone.flattr4j.model.result.FlattrResult#getMessage()
   */
  @Override
  public Message getMessage() {
    if( message == null ) {
      message = Message.forMessage(data.get("message"));
    }
    return message;
  }
  
  /* (non-Javadoc)
   * @see org.shredzone.flattr4j.model.result.FlattrResult#getDescription()
   */
  @Override
  public String getDescription() {
    if( description == null ) {
      description = data.get("description");
    }
    return description;
  }
  
  /* (non-Javadoc)
   * @see org.shredzone.flattr4j.model.result.ThingResult#getLocation()
   */
  @Override
  public String getLocation() {
    if( location == null ) {
      location = data.get("link");
    }
    return location;
  }
  
  /* (non-Javadoc)
   * @see org.shredzone.flattr4j.model.result.FlattrResult#getThing()
   */
  @Override
  public Thing getThing() {
    if( thing == null ) {
      thing = new Thing(data.getFlattrObject("thing"));
    }
    return thing;
  }

  /*
   * (non-Javadoc)
   * @see org.shredzone.flattr4j.model.ThingId#getThingId()
   */
  @Override
  public String getThingId() {
    return getThing().getThingId();
  }

  @Override
  public String getId() {
    if( id == null ) {
      id = data.get("id");
    }
    return id;
  }

}
