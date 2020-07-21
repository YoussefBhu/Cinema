import React from 'react';
import {Container , Row , Col , Button , Card , Accordion , Image , ButtonGroup , Form} from "react-bootstrap"
import 'bootstrap/dist/css/bootstrap.min.css'
export default class Salle extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      Projections : [] , 
      Selected : null , 
      loading : false , 
      ProjectionButtons : [], 
      PlacesButtons : [] , 
      SelectedPlaces : [] , 
      Nom : "", 
      Code : ""
    };

    
  }
  async  GetData(){
      var res 
    await fetch(this.props.salle._links.projection.href).then(response => response.json())
      .then(responseJson => {
        res =  responseJson._embedded.projections
      }).catch(error => alert(error)); 
    return res 
    }

  async SetFilmsTickets(Projections){
    for (var i = 0  ; i< Projections.length ; i++){
      await fetch(Projections[i]._links.film.href).then(response => response.json())
      .then(responseJson => { 
          Projections[i].film = responseJson 
      }).catch(error => alert(error)); 

      await fetch(Projections[i]._links.tickets.href).then(response => response.json())
      .then(responseJson => { 
          Projections[i].Tickets = responseJson._embedded.tickets
      }).catch(error => alert(error)); 

      Projections[i].reservedPlaces = []

      for(var x = 0  ; x <  Projections[i].Tickets.length ; x++){
          var ticket = Projections[i].Tickets[x]
          if(Projections[i].Tickets[x].reserve){
            Projections[i].reservedPlaces.push(Projections[i].Tickets[x].id)
          }
      }

      await fetch(Projections[i]._links.seance.href).then(response => response.json())
      .then(responseJson => { 
          Projections[i].heureDebut = responseJson.heureDebut
      }).catch(error => alert(error)); 
    }
    return Projections
  }

  setSeances(){
    var buttons = []
    for(var i = 0  ; i < this.state.Projections.length ; i++){
      var variant = "outline-primary"
      if(i ==  0 ){variant = "primary"}
      var projection = this.state.Projections[i]
      buttons.push(<Button style={{height:50 }} id={i} variant={variant} onClick={e => this.SelectProjection(e, "id")}>{projection.heureDebut.slice(11,16)+" ("+projection.prix+" DH)"} </Button>)
    }
    this.setState({ProjectionButtons : buttons })
  }

  async SelectProjection(e){
    this.setState({Selected : this.state.Projections[e.target.id]})
    var buttons = []
    for(var i = 0  ; i < this.state.Projections.length ; i++){
      var projection = this.state.Projections[i]
      var variant = "outline-primary"
      if(i ==  e.target.id ){variant = "primary"}
      buttons.push(<Button style={{height:50 }} id={i} variant={variant} onClick={e => this.SelectProjection(e, "id")}>{projection.heureDebut.slice(11,16)+" ("+projection.prix+" DH)"} </Button>)
    }
    
    await this.setState({SelectedPlaces : []})
    await this.setState({ProjectionButtons : buttons })
    await this.setPlaces()
  }

  async componentDidMount() {
    var Projection = await this.GetData() ; 
    this.setState({Projections : await this.SetFilmsTickets(Projection)}) 
  //console.log(this.state.Projections)
    await this.setState({Selected : this.state.Projections[0]})
    await this.setSeances()
    await this.setPlaces()
    this.setState({loading : true})
  } 

 async setPlaces(){
   var Buttons = []
   var SelectedPlaces = this.state.Selected.reservedPlaces
      for (var i = 0 ; i < this.state.Selected.Tickets.length ; i++){
        
          if(SelectedPlaces.includes(this.state.Selected.Tickets[i].id)){
            Buttons.push(<Button  style={{margin : 5 , width : 50 , justifyContent : "center" }} variant="secondary" disabled>{i+1}</Button>)
          }
          else {
            Buttons.push(<Button id={this.state.Selected.Tickets[i].id} style={{margin : 5 , width : 50 , justifyContent : "center" }} variant="primary"  onClick={e => this.changecolor(e , "id")} >{i+1}</Button>)
          }
      
      }
      this.setState({PlacesButtons : Buttons})
  }

async changecolor(e){
  var buttons = this.state.PlacesButtons
  var SelectedPlaces = this.state.SelectedPlaces
  var i = e.target.id
  var x = e.target.innerText
  if(SelectedPlaces.includes(i)){
    buttons[x-1] = <Button id={i} style={{margin : 5 , width : 50 , justifyContent : "center" }} variant="primary"  onClick={e => this.changecolor(e , "id")} >{x}</Button>
    SelectedPlaces.splice(SelectedPlaces.indexOf(i), 1)
  }else{
    buttons[x-1] = <Button id={i} style={{margin : 5 , width : 50 , justifyContent : "center" }} variant="success"  onClick={e => this.changecolor(e , "id")} >{x}</Button>
    SelectedPlaces.push(i)
  }
  this.setState({SelectedPlaces :  SelectedPlaces})
  this.setState({PlacesButtons : buttons})
  console.log(this.state.SelectedPlaces)
}

async send(){
  if(this.state.Nom == "" || this.state.Code == "" || this.state.SelectedPlaces.length == 0){
      alert("entrer les données et choisi vos places")
  }
  else {


  var data = {
    nomClient : this.state.Nom ,
    codePayement : this.state.Code ,
    tickets : this.state.SelectedPlaces  
}
    await fetch("http://localhost:8080/payerTickets", {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),})
        .then(response => response.json())
        .then(responseJson => {
            alert("success : paiement effectuer")    
            this.setState({SelectedPlaces : []})
          
        }); 
        var Projection = await this.GetData() ; 
        this.setState({Projections : await this.SetFilmsTickets(Projection)}) 
        await this.setState({Selected : this.state.Projections[0]})
        await this.setSeances()
        await this.setPlaces()
      }
}
    render() {
      if(this.state.loading){
   
        return (

        <Card style={{width : "45%" , margin:'1%',justifyContent : "center" }}>
          <Card.Header style={{position : "inline"}}><h5 style={{color : "blue"}}>{this.props.salle.name} : {this.state.Selected.film.titre}</h5></Card.Header>
          <Card.Body style={{paddingRight : "5%" , paddingLeft : "5%"}}>
          <Row style={{display : "flex" }}>
            <Image style={{width : "30%" , height : "30%"}} src={require("../Images/"+this.state.Selected.film.photo)}/>
            <Card style={{width : "45%" ,  marginLeft: "auto" , height : "100%" }}>
            <Card.Header>Séances</Card.Header>
            <ButtonGroup vertical>
            {this.state.ProjectionButtons}
            </ButtonGroup>
            </Card>
          </Row>
          <Row style={{display : "flex" ,  border: '1px solid grey' , marginTop : 20 , padding : 20 , borderRadius : 20}}> 
          <div style={{ justifyContent : "center" , width : "50%" }}>
            <h4>Choisi vos places</h4>
            <Row style={{}}>
            {this.state.PlacesButtons}
            </Row>
            </div>
            <Form style={{width : "40%" ,  marginLeft: "auto" , height : "100%"}}>
            <Form.Group >
            <Form.Label>Nom et prenom</Form.Label>
            <Form.Control   placeholder="Nom et prenom" onChange = {(event) => this.setState({Nom : event.target.value })}/>
            </Form.Group>
            <Form.Group >
            <Form.Label>Code de payement</Form.Label>
            <Form.Control value={this.state.value}  placeholder="Code de payement" onChange = {(event) => this.setState({Code : event.target.value })} />
            </Form.Group>
            <Button variant="primary"  onClick={()=> this.send()} >
            Submit
            </Button>
            </Form>
            </Row>
           
          </Card.Body>
          </Card>)
    }
    
  else {
    return (<h1>Loading</h1>)
  }
}
}