import { useEffect, useState } from "react";
import BookList from "./BookList";
import CustomerSidebar from "./CustomerSidebar";
import { BrowserRouter as Router, Route, Routes, useLocation } from 'react-router-dom';
import useFetch from "./useFetch";
import { Customer } from "./DTO/Customer";
import detailsImage from './img/details1.jpg';
import TextInput from "./inputs/TextInput";
import PasswordInput from "./inputs/PasswordInput";
import DTO from "./DTO/DTO";
import { read } from "fs";



type Props = {
    setDisplayLogoutText: React.Dispatch<React.SetStateAction<boolean>>;
}

const CustomerPage = (props: Props) => {
    const [isBorrowsUrl, setIsBorrowsUrl] = useState(true);
    const [isDetailsUrl, setIsDetailsUrl] = useState(true);
    const [isSettingsUrl, setIsSettingsUrl] = useState(true);

    const location = useLocation();

    useEffect( () => {
        const url = location.pathname;
        
        switch (url) {
            case '/customer/borrows': {
                setIsBorrowsUrl(true);
                setIsDetailsUrl(false);
                setIsSettingsUrl(false);

                break;
            }
            case '/customer/details': {
                setIsDetailsUrl(true);
                setIsBorrowsUrl(false);
                setIsSettingsUrl(false);

                break;
            }
            case '/customer/settings': {
                setIsSettingsUrl(true);
                setIsBorrowsUrl(false);
                setIsDetailsUrl(false);

                break;
            }
        }

    }, [location])

    return ( 
    <main className="customerPage">
        <CustomerSidebar setDisplayLogoutText={props.setDisplayLogoutText}/>
        {isBorrowsUrl && <section className="borrows"><BookList url='http://localhost:8000/books' isBorrowBtn={false} isReturnBtn={true}/></section>}
        {isDetailsUrl && <Details />}
        {isSettingsUrl && <Settings />}
    </main> 
    );
}

const Details = () => {
    const customerDetails: Customer = useFetch('http://localhost:8000/users/' + sessionStorage.getItem("id")) as unknown as Customer;

    return (
        <section className="details">
            <h2 className="details__title">Account details</h2>
            <p className="details__data details__data--first-name">First name: <span className="details__field">{customerDetails.firstName}</span></p>
            <p className="details__data details__data--last-name">Last name: <span className="details__field">{customerDetails.lastName}</span></p>
            <p className="details__data details__data--email">Email: <span className="details__field">{customerDetails.email}</span></p>
            <p className="details__data details__data--phone">Phone: 
                {customerDetails.phone ? <span className="details__field">{customerDetails.phone}</span> : <span className="details__field">- - -</span>}
            </p>
            <span className="details__image"><div></div></span>
        </section>
    )
}

const Settings = () => {
    const customerDetails = useFetch('http://localhost:8000/users/' + sessionStorage.getItem("id")) as unknown as Customer;

    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [phone, setPhone] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const [readyToRender, setReadyToRender] = useState(false);

    useEffect( () => {

        if (Object.keys(customerDetails).length !== 0) {

        setFirstName(customerDetails.firstName);
        setLastName(customerDetails.lastName);
        setPhone(customerDetails.phone);
        setEmail(customerDetails.email);
        setPassword(customerDetails.password);

        setReadyToRender(true);

        }

    },[customerDetails])

    const handleEdit = () => {
        const page = document.querySelector(".App");
    }

    return (
        <section className="settings">
            <h2 className="settings__title">Account settings</h2>
            {readyToRender &&   
            <div className="settings__content">
                <p className = "settings__data settings__data--first-name">
                    <label htmlFor="firstName"><span className="settings__label-description">First name:</span> {firstName} </label>
                    <button className="btn btn--edit">Edit</button>
                </p>
                <p className = "settings__data settings__data--last-name">
                    <label htmlFor="lastName"><span className="settings__label-description">Last name:</span> {lastName} </label>
                    <button className="btn btn--edit">Edit</button>
                </p>
                <p className = "settings__data settings__data--email">
                    <label htmlFor="email"><span className="settings__label-description">Email:</span> {email}</label>
                    <button className="btn btn--edit">Edit</button>
                </p>
                <p className = "settings__data settings__data--phone">
                    {customerDetails.phone ? <label htmlFor="phone"><span className="settings__label-description">Phone:</span> {phone}</label> : <label htmlFor="phone"><span className="settings__label-description">Phone:</span> - - - </label>}
                    <button className="btn btn--edit">Edit</button>
                </p>
                <p className = "settings__data settings__data--password">
                    <label htmlFor="password">Password</label>
                    <button className="btn btn--edit">Change</button>
                </p>
            </div>
            }
            <span className="settings__image"><div></div></span>
        </section>
    )
}

type EditContentProps = {
    state: string;
    setState: React.Dispatch<React.SetStateAction<string>>;
}

const EditContent = (props: EditContentProps) => {
    return ( 
        <div className="edit__background">
            <section className="edit__content">
                <h2 className="edit__title">Edit {props.state}</h2>
                <label className="edit__label">{props.state}</label>
                <TextInput state={props.state} setState={props.setState} isRequired={true} placeholder={props.state}/>
                <button className="btn">Cancel</button>
                <button className="btn">Save</button>
            </section>
        </div>
     );
}
 

export default CustomerPage;