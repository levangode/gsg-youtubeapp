import {Component, Input, OnInit} from '@angular/core';
import {AuthenticationService} from "../_services/authentication.service";
import {User} from "../_models/user";
import {interval, Observable, Subscription} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {UserService} from "../_services/user.service";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  currentUser: User;
  currentUserSubscription: Subscription;
  videoUrl: any;
  prevId: any;

  constructor(
    private sanitizer: DomSanitizer,
    private authenticationService: AuthenticationService,
    private userService: UserService
  ) {
    this.currentUserSubscription = this.authenticationService.currentUser.subscribe(user => {
      this.currentUser = user;
    });
  }

  ngOnInit() {
    interval(1000).subscribe(value => {
      this.userService.getVideoId().subscribe(value => {
        let videoId = value['topVideo'];
        if (videoId === this.prevId) {
        } else {
          this.videoUrl = this.sanitizer.bypassSecurityTrustResourceUrl("https://www.youtube.com/embed/" + videoId);
          this.prevId = videoId;
        }
      });
    })
  }

}
