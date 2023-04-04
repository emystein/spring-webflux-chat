import React, {useState} from 'react';
import {sendMessage} from '../actions/chat';
import {connect} from 'react-redux';

function MessageInput({user, sendMessage}) {
  const [message, setMessage] = useState("");

  const onSubmit = (event) => {
    event.preventDefault();
    sendMessage(user, message);
    setMessage("");
    return false;
  }

  return (
    <div className="container-fluid">
      <form className="form-horizontal" role="form" onSubmit={event => onSubmit(event)}>
        <div className="form-group">
          <div className="input-group">
            <div className="input-group-addon">Type Message</div>
            <input type="text"
                   value={message}
                   onChange={event => setMessage(event.target.value)}
                   className="form-control input-lg"></input>
          </div>
        </div>
      </form>
    </div>
  );
}

export default connect(({user}) => ({user}), {sendMessage})(MessageInput);
