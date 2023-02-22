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
    const userDetails: Customer = useFetch('http://localhost:8000/users/' + sessionStorage.getItem("id")) as unknown as Customer;

    return (
        <section className="details">
            <h2 className="details__title">Account details</h2>
            <p className="details__data details__data--first-name">First name: <span className="details__field">{userDetails.firstName}</span></p>
            <p className="details__data details__data--last-name">Last name: <span className="details__field">{userDetails.lastName}</span></p>
            <p className="details__data details__data--email">Email: <span className="details__field">{userDetails.email}</span></p>
            <p className="details__data details__data--phone">Phone: 
                {userDetails.phone ? <span className="details__field">{userDetails.phone}</span> : <span className="details__field">- - -</span>}
            </p>
            <span className="details__image"><div></div></span>
        </section>
    )
}



const Settings = () => {
    const userDetails = useFetch('http://localhost:8000/users/' + sessionStorage.getItem("id")) as unknown as Customer;

    // const [user, setUser] = useState<Customer>({
    //     id: 0,
    //     firstName: "",
    //     lastName: "",
    //     phone: "",
    //     email: "",
    //     password: ""
    // });
     
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [phone, setPhone] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const [readyToRender, setReadyToRender] = useState(false);


    useEffect( () => {

        if (Object.keys(userDetails).length !== 0) {

        setFirstName(userDetails.firstName);
        setLastName(userDetails.lastName);
        setPhone(userDetails.phone);
        setEmail(userDetails.email);
        setPassword(userDetails.password);

        // setUser(userDetails);
        
        setReadyToRender(true);
        }

    },[userDetails])


    const handleEditData = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, setShowEditData: React.Dispatch<React.SetStateAction<boolean>>) => {
        e.preventDefault();

        const user: Customer = {
            id: sessionStorage.getItem('id') as unknown as number,
            firstName: firstName,
            lastName: lastName,
            phone: phone,
            email: email,
            password: password
         }

        fetch('http://localhost:8000/users/' + sessionStorage.getItem("id"), {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(user)
        })

        setShowEditData(false);
    }


    return (
        <section className="settings">
            <h2 className="settings__title">Account settings</h2>
            {readyToRender &&  
                <div className="settings__content">
                    <PersonalInformation data={firstName} setData={setFirstName} htmlName="first-name" displayName="First name" handleEditData={handleEditData}/>
                    <PersonalInformation data={lastName} setData={setLastName} htmlName="last-name" displayName="Last name" handleEditData={handleEditData}/>
                    <PersonalInformation data={email} setData={setEmail} htmlName="email" displayName="Email" handleEditData={handleEditData}/>
                    <PersonalInformation data={phone} setData={setPhone} htmlName="phone" displayName="Phone" handleEditData={handleEditData}/>
                    <PersonalInformation data={password} setData={setPassword} htmlName="password" displayName="Password" handleEditData={handleEditData} buttonName="Change"/>
                </div>     
            }
            <span className="settings__image"><div></div></span>
        </section>
    )
}

type PersonalInformation = {
    data: string;
    setData: React.Dispatch<React.SetStateAction<string>>;
    htmlName: string;
    displayName: string;
    handleEditData: (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, setShowEditData: React.Dispatch<React.SetStateAction<boolean>>) => void;
    buttonName?: string;
}

const PersonalInformation = (props: PersonalInformation) => {

    const[showEditPanel, setShowEditPanel] = useState(false);

    const handleClickEditBtn = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        e.preventDefault();
        setShowEditPanel(true);
    }

    return (
        <div className = {"settings__data settings__data--"+props.htmlName}>
            <label htmlFor={props.htmlName}>
                <span className="settings__label-description">{props.displayName}:</span>
                {props.data ? (
                    <span>{props.data}</span>
                ) : (
                    <span>- - -</span>
                )} 
            </label>
            <button 
                className="btn btn--edit" 
                onClick={ (e) => {
                    handleClickEditBtn(e)
                }}
            >
               {props.buttonName ? (
                    <span>{props.buttonName}</span>
               ) : (
                    <span>Edit</span>
               )} 
            </button>
            {showEditPanel && (
                    <EditPanel 
                        data={props.data}
                        setData={props.setData} 
                        setShowEditPanel={setShowEditPanel} 
                        displayName={props.displayName} 
                        handleEditData={props.handleEditData} 
                    />
                )
            }
        </div>
    )
}

type EditPanelProps = {
    data: string;
    setData: React.Dispatch<React.SetStateAction<string>>;
    setShowEditPanel: React.Dispatch<React.SetStateAction<boolean>>;
    displayName: string;
    handleEditData: (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, setShowEditData: React.Dispatch<React.SetStateAction<boolean>>) => void;
}

const EditPanel = (props: EditPanelProps) => {
    const [test, setTest] = useState('');

    const handleCancelOperation = () => {
        props.setShowEditPanel(false);
    } 

    return ( 
        <div className="edit__container">
            <section className="edit__content">
                <h2 className="edit__title">Edit {props.displayName}</h2>
                <div className="edit__input-container">
                    <label className="edit__label">{props.displayName}</label>
                    <TextInput 
                        state={props.data} 
                        setState={props.setData} 
                        isRequired 
                        placeholder={props.data}/>
                </div>
                <button className="btn btn--cancel" onClick={handleCancelOperation}>Cancel</button>
                <button className="btn btn--save" 
                    onClick={ (e) => {
                        props.handleEditData(e, props.setShowEditPanel);
                    }}
                >Save</button>
            </section>
        </div>
     );
}
 

export default CustomerPage;