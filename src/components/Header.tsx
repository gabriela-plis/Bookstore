import { Session } from "inspector";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { BrowserRouter } from "react-router-dom";
import logout from "../functions/logout";

type HeaderProps = {
    sessionIsActive: boolean;
    setDisplayLogoutText: React.Dispatch<React.SetStateAction<boolean>>;
}

const Header = (props: HeaderProps) => {

    return ( 
        <header className="header">
            <h1 className="header__title">Bookstore</h1>
            <nav>
                <ul className="header__navbar">
                    <li><Link to="/">Home</Link></li>
                    {!props.sessionIsActive && <li><Link to="/logIn">Log In</Link></li>}
                    {!props.sessionIsActive && <li><Link to="/register">Register</Link></li>}
                    {props.sessionIsActive && <li><Link to="/user/borrows">My Account</Link></li>}
                    {props.sessionIsActive && <li><Link to="/" onClick={() => logout(props.setDisplayLogoutText)}>Log Out</Link></li>}   
                </ul>
            </nav>
        </header>
     );
}
 
export default Header;