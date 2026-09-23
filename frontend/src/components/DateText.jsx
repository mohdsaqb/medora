import React from 'react';

/**
 * Renders a date without pulling in Moment.
 *
 * Replaces react-moment, which depends on moment. Moment is in maintenance
 * mode and neither package ships a React 19 build. Intl.DateTimeFormat is
 * built into the browser and covers the three formats this app used.
 */
export default function DateText({ value, withTime = false }) {
	if (!value) {
		return null;
	}

	const date = new Date(value);
	if (Number.isNaN(date.getTime())) {
		return null;
	}

	const options = { day: '2-digit', month: 'short', year: 'numeric' };
	if (withTime) {
		options.hour = '2-digit';
		options.minute = '2-digit';
	}

	return <>{new Intl.DateTimeFormat('en-IN', options).format(date)}</>;
}
