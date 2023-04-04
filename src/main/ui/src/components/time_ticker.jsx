import React, {useEffect} from 'react';
import {connect} from 'react-redux';
import {createTimer, removeTimer} from '../actions/time';

function TimeTicker({interval, createTimer, removeTimer}) {
  useEffect(() => {
    console.log('TimeTicker did mount');
    createTimer();

    // called on unmount
    return () => {
      console.log('TimeTicker will unmount');
      removeTimer(interval);
    }
  });

  return false;
}

export default connect(({time: {interval}}) => ({interval}), {createTimer, removeTimer})(TimeTicker);
