import React, { useState } from 'react';
import '../styles/login.scss';
import { joinChat } from '../actions/chat';
import { connect } from 'react-redux';
import { useNavigate } from "react-router-dom";

const DEFAULT_AVATAR = '//ssl.gstatic.com/accounts/ui/avatar_2x.png';

function Login({joinChat}) {
  const navigate = useNavigate();

  const [alias, setAlias] = useState('');
  const [avatar, setAvatar] = useState(DEFAULT_AVATAR);

  const updateAlias = (newAlias) => {
    setAlias(newAlias);
    const avatar = newAlias ? encodeURI(`https://robohash.org/${newAlias.toLowerCase()}.png`) : DEFAULT_AVATAR;
    setAvatar(avatar);
  }

  const handleSubmit = (event) => {
    event.preventDefault();
    joinChat({ alias, avatar });
    navigate('/chat');
    return false;
  }

  return (
    <div className="container">
      <div className="panel panel-default card card-container">
        <img id="profile-img" className="profile-img-card" src={avatar} />
        <p id="profile-name" className="profile-name-card"></p>
        <form className="form" onSubmit={handleSubmit}>
          <div className="form-group">
            <input type="text"
              className="form-control input-lg"
              placeholder="Alias"
              value={alias}
              onChange={(e) => updateAlias(e.target.value)}
              required autoFocus />
          </div>
          <div className="form-group">
            <button className="btn btn-lg btn-success btn-block" type="submit">Chat</button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default connect(null, {joinChat})(Login);
