type TextInputProps = {
    name: string;
    state: string;
    setState: (e: React.ChangeEvent<HTMLInputElement>) => void;
    isRequired: boolean;
    placeholder?: string;
  };

const TextInput = (props: TextInputProps) => {
    return (
        <input 
            type="text"
            name={props.name}
            required={props.isRequired}
            value={props.state ?? ''} 
            onChange={(e) => {props.setState(e)}}
            placeholder={props.placeholder?? ''}
        />
    )
}
 
export default TextInput;