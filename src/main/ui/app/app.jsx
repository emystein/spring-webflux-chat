import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';

import Chat from './components/chat';
import Home from './components/home';
import Login from './components/login';

const App = () => {
  return(
          <Router>
            <div className="full-height">
              <Route exact path="/" component={Login}/>
              <Route exact path="/chat" component={Chat}/>
            </div>
          </Router>
  );
}
export default App;