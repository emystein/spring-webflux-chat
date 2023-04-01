import React from 'react';
import {connect} from 'react-redux';
import moment from 'moment';

function HumanizedTime({time, date}) {
    if (!time.now) {
        return (<div/>);
    }

    const timeAgo = moment.duration(time.now.getTime() - date);
    
    return (
        <span>{timeAgo.humanize()} ago</span>
    );
}

export default connect(({time}) => ({time}))(HumanizedTime);
