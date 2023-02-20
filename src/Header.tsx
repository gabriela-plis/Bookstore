import { Session } from "inspector";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { BrowserRouter } from "react-router-dom";
import logout from "./logout";

type HeaderProps = {
    sessionIsActive: boolean;
    setSessionIsActive: React.Dispatch<React.SetStateAction<boolean>>;
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
                    {props.sessionIsActive && <li><Link to="/customer/borrows">My Account</Link></li>}
                    {props.sessionIsActive && <li><Link to="/" onClick={() => logout(props.setSessionIsActive)}>Log Out</Link></li>}   
                </ul>
            </nav>
        </header>
     );
}
 
export default Header;