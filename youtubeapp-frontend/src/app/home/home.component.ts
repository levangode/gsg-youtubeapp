import {Component, Input, OnInit} from '@angular/core';
import {AuthenticationService} from "../_services/authentication.service";
import {User} from "../_models/user";
import {interval, Observable, Subscription} from "rxjs";
import {UserService} from "../_services/user.service";
import {DomSanitizer} from "@angular/platform-browser";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {first} from "rxjs/operators";
import {Router} from "@angular/router";
import {AlertService} from "../_services/alert.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  currentUser: User;
  currentUserSubscription: Subscription;
  videoUrl: any;
  comment: any;
  prevComment: any;
  prevId: any;
  updateForm: FormGroup;
  loading = false;
  submitted = false;
  minInterval = 1;
  maxInterval = 60;
  youtubeEmbedUrl = "https://www.youtube.com/embed/";

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private alertService: AlertService,
    private sanitizer: DomSanitizer,
    private authenticationService: AuthenticationService,
    private userService: UserService
  ) {
    this.currentUserSubscription = this.authenticationService.currentUser.subscribe(user => {
      this.currentUser = user;
    });
  }

  ngOnInit() {
    this.updateForm = this.formBuilder.group({
      country: ['', Validators.required],
      jobInterval: ['', [Validators.required, Validators.min(1), Validators.max(60)]]
    });

    this.userService.getUser().subscribe(value => {
      this.form['country'].setValue(value['country']);
      this.form['jobInterval'].setValue(value['jobInterval']);
    });

    interval(1000).subscribe(value => {
      this.userService.getUser().subscribe(value => {
        let videoId = value['topVideo'];
        let comment = value['topComment'];
        if (videoId === this.prevId) {
        } else {
          this.videoUrl = this.sanitizer.bypassSecurityTrustResourceUrl(this.youtubeEmbedUrl + videoId);
          this.prevId = videoId;
        }
        if(comment === this.prevComment){
        } else {
          this.comment = comment;
          this.prevComment = comment;
        }
      });
    })
  }

  get form() {
    return this.updateForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    if (this.updateForm.invalid) {
      return;
    }

    this.loading = true;
    this.userService.update(this.updateForm.value)
      .pipe(first())
      .subscribe(
        data => {
          this.alertService.success('Update successful', false);
          this.loading = false;
        },
        error => {
          this.alertService.error(error);
          this.loading = false;
        });
  }

}
