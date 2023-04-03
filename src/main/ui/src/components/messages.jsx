import React from 'react';
import {connect} from 'react-redux';
import HumanizedTime from './humanized_time';
import '../styles/messages.scss';

function Messages({messages}) {
  const renderMessages = () => {
    return messages.map(message => {
      return (
        <div key={message.id} className="list-group-item">
          <div className="media">
            <div className="media-left">
              <img className="media-object img-circle" src={message.user.avatar}/>
            </div>
            <div className="media-body">
              <div className="row">
                <div className="col-md-2 text-left text-info">
                  {message.user.alias}
                </div>
                <div className="col-md-8 text-left">{message.message}</div>
                <div className="col-md-2 text-right text-info">
                  <small><HumanizedTime date={message.timestamp}/></small>
                </div>
              </div>
            </div>
          </div>
        </div>
      );
    });
  }

  return (
    <div className="list-group chat-messages panel">
      {renderMessages()}
      <div ref={(div) => { if (div) div.scrollIntoView({block: 'end', behavior: 'smooth'}); }}/>
    </div>
  );
}

function mapStateToProps(state) {
  return {messages: state.messages};
}

export default connect(mapStateToProps)(Messages);
