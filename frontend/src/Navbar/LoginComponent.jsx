import React, { Component } from 'react'
import axios from 'axios'
import AuthService from '../services/AuthService'

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8185/api';

/**
 * Sign in control for the navbar.
 *
 * Reads are public, so signing in is only needed to create, edit or delete.
 * Credentials are checked by calling /user/me, which returns the role on
 * success and 401 on failure.
 */
export default class LoginComponent extends Component {

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            role: AuthService.getRole(),
            error: '',
            busy: false
        };
    }

    handleChange = (event) => {
        this.setState({ [event.target.name]: event.target.value });
    }

    signIn = (event) => {
        event.preventDefault();
        const { username, password } = this.state;
        this.setState({ busy: true, error: '' });

        axios.get(API_BASE_URL + '/user/me', {
            headers: { Authorization: 'Basic ' + btoa(username + ':' + password) }
        }).then((response) => {
            AuthService.signIn(username, password, response.data.role);
            this.setState({ role: response.data.role, password: '', busy: false });
        }).catch(() => {
            this.setState({ error: 'Incorrect username or password', busy: false });
        });
    }

    signOut = () => {
        AuthService.signOut();
        this.setState({ role: null, username: '', password: '' });
    }

    render() {
        if (this.state.role) {
            return (
                <span className="navbar-text ml-auto">
                    <span className="text-light mr-2">
                        {AuthService.getSession().username} ({this.state.role})
                    </span>
                    <button className="btn btn-sm btn-outline-light" onClick={this.signOut}>
                        Sign out
                    </button>
                </span>
            )
        }

        return (
            <form className="form-inline ml-auto" onSubmit={this.signIn}>
                <input
                    className="form-control form-control-sm mr-1"
                    type="text"
                    name="username"
                    placeholder="Username"
                    autoComplete="username"
                    value={this.state.username}
                    onChange={this.handleChange}
                />
                <input
                    className="form-control form-control-sm mr-1"
                    type="password"
                    name="password"
                    placeholder="Password"
                    autoComplete="current-password"
                    value={this.state.password}
                    onChange={this.handleChange}
                />
                <button className="btn btn-sm btn-outline-light" type="submit" disabled={this.state.busy}>
                    {this.state.busy ? 'Signing in' : 'Sign in'}
                </button>
                {this.state.error &&
                    <span className="text-warning ml-2">{this.state.error}</span>}
            </form>
        )
    }
}
