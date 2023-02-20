import { useEffect, useState } from "react";
import BookList from "./BookList";
import CustomerSidebar from "./CustomerSidebar";
import { BrowserRouter as Router, Route, Routes, useLocation } from 'react-router-dom';
import useFetch from "./useFetch";
import { Customer } from "./DTO/Customer";
import detailsImage from './img/details1.jpg';



type Props = {
    setSessionIsActive: React.Dispatch<React.SetStateAction<boolean>>
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
        <CustomerSidebar setSessionIsActive={props.setSessionIsActive}/>
        {isBorrowsUrl && <section className="borrows"><BookList url='http://localhost:8000/books' isBorrowBtn={false} isReturnBtn={true}/></section>}
        {isDetailsUrl && <Details />}
        {isSettingsUrl && <Settings />}
    </main> 
    );
}

const Details = () => {
    const userDetails: Customer = useFetch('http://localhost:8000/users/' + sessionStorage.getItem("id")) as unknown as Customer;
    console.log(userDetails.phone)

    return (
        <section className="details">
            <h2 className="details__title">Account details</h2>
            <p className="details__data details__data--first-name">First name: <span className="details__field">{userDetails.firstName}</span></p>
            <p className="details__data details__data--last-name">Last name: <span className="details__field">{userDetails.lastName}</span></p>
            <p className="details__data details__data--email">Email: <span className="details__field">{userDetails.email}</span></p>
            <p className="details__data details__data--phone">Phone: 
                {userDetails.phone ? <span className="details__field">{userDetails.phone}</span> : <span className="details__field">- - -</span>}
            </p>
            <div className="details__image"></div>
        </section>
    )
}

const Settings = () => {
    return (
        <section className="settings">
            <p>settings</p>
        </section>
    )
}
 
export default CustomerPage;