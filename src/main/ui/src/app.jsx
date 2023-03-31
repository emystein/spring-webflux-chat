import React, { Component } from 'react';
import { connect } from 'react-redux';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import TimeTicker from './components/time_ticker';
import Chat from './components/chat';
import Login from './components/login';
import requireUser from './components/require_user';
import { connectToChatServer } from './actions/chat';

class App extends Component {
  componentDidMount() {
    this.props.connectToChatServer(`ws://${location.host}/websocket/chat`);
  }

  render() {
    return (
      <div className="full-height">
        <Router>
          <Routes>
            <Route path="/" element={<Login/>}/>
            {/* <Route path="/chat" element={requireUser(Chat)}/> */}
            <Route path="/chat" element={<Chat/>}/>
          </Routes>
        </Router>
        <TimeTicker />
      </div>
    );
  }
}

export default connect(null, { connectToChatServer })(App);
