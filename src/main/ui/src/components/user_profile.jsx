import React from 'react';
import {connect} from 'react-redux';
import '../styles/profile.scss';

function UserProfile({user}) {
  return (
    <div className="list-group user-profile">
      <div className="list-group-item">
        <img src={user.avatar} className="img-responsive img-circle center-block profile-image"/>
      </div>
      <div className="list-group-item">
        <p className="text-center">{user.alias}</p>
      </div>
    </div>
  );
}

export default connect(({user}) => ({user}))(UserProfile);
