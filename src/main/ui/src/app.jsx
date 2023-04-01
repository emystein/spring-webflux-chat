import React, {useState, useEffect} from 'react';
import {connect} from 'react-redux';
import {Navigate, BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import TimeTicker from './components/time_ticker';
import Chat from './components/chat';
import Login from './components/login';
import {connectToChatServer} from './actions/chat';

function App({connectToChatServer}) {
    const [loggedIn, setLoggedIn] = useState(false);

    // Set connectToChatServer as dependency (second parameter of useEffect) so it's called just once.
    // See: https://react.dev/reference/react/useCallback#preventing-an-effect-from-firing-too-often
    useEffect(() => {
        connectToChatServer(`ws://${location.host}/websocket/chat`);
    }, [connectToChatServer]);

    return (
        <div className="full-height">
            <Router>
                <Routes>
                    <Route path="/" element={<Login confirm={setLoggedIn}/>}/>
                    <Route path="/chat" element={loggedIn ? <Chat /> : <Navigate replace to={"/"} />}/>
                </Routes>
            </Router>
            <TimeTicker/>
        </div>
    );
}

export default connect(null, { connectToChatServer })(App);
