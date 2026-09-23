import React, { Component } from 'react'
import LoginComponent from './LoginComponent'
import AuthService from '../services/AuthService'

export default class NavbarComponent extends Component {
    render() {
        return (
            <div className="sticky-top">
                <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
                    <a className="navbar-brand" href="/">Medora</a>
                    <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarNav">
                        <ul className="navbar-nav">
                            <li className="nav-item dropdown">
                                <a className="nav-link dropdown-toggle" href="/" id="navbarDropdownMenuLink" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                Patients
                                </a>
                                <div className="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                                    <a className="dropdown-item"  href="/patients">Patients</a>
                                    {AuthService.canEditPatients() &&
                                        <a className="dropdown-item" href="/add-patient">Add Patient</a>}
                                </div>
                            </li>
                        </ul>
                        <LoginComponent />
                    </div>
                </nav>
            </div>
        )
    }
}
